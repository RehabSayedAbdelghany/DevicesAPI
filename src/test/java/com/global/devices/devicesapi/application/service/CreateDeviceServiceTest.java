package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.DeviceRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDeviceServiceTest {

    // Mock the dependency (the repository wrapper)
    @Mock
    private DeviceRepositoryWrapper repository;

    // Inject the mock into the class being tested
    @InjectMocks
    private CreateDeviceService createDeviceService;

    private DeviceUseCaseRequest request;
    private Device mockSavedDevice;
    private final UUID TEST_ID = UUID.randomUUID();
    private final Instant TEST_TIME = Instant.now();

    @BeforeEach
    void setUp() {
        // 1. Arrange the incoming request DTO
        request = DeviceUseCaseRequest.builder()
                .name("New Laptop")
                .brand("TechCorp")
                .state(DeviceState.AVAILABLE)
                .build();

        // 2. Arrange the expected output from the repository (the saved Device domain object)
        // This simulates the saved object having its ID and creationTime assigned.
        mockSavedDevice = new Device(
                TEST_ID,
                request.getName(),
                request.getBrand(),
                request.getState(),
                TEST_TIME
        );
    }

    @Test
    void create_ShouldCreateDeviceAndReturnMappedResponse() {
        // Arrange
        // Mock the repository call: when saveDevice is called with ANY Device, return the mockSavedDevice
        when(repository.saveDevice(any(Device.class))).thenReturn(mockSavedDevice);

        // Act
        DeviceUseCaseResponse response = createDeviceService.create(request);

        // Assert 1: Verify delegation to the repository
        // We use ArgumentCaptor to inspect the Device object passed to the repository's save method
        verify(repository, times(1)).saveDevice(any(Device.class));

        // Assert 2: Verify the response mapping is correct
        assertNotNull(response, "The response should not be null.");
        assertEquals(TEST_ID, response.getId(), "The ID of the response should match the saved device ID.");
        assertEquals(request.getName(), response.getName(), "Name should be mapped correctly.");
        assertEquals(request.getBrand(), response.getBrand(), "Brand should be mapped correctly.");
        assertEquals(request.getState(), response.getState(), "State should be mapped correctly.");
        assertEquals(TEST_TIME, response.getCreationTime(), "CreationTime should be mapped correctly.");
    }
}