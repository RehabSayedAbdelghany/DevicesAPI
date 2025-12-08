package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.DeviceRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.exception.DeviceNotFoundException;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDeviceServiceTest {

    @Mock
    private DeviceRepositoryWrapper repository;

    @InjectMocks
    private GetDeviceService getDeviceService;

    private final UUID TEST_ID = UUID.randomUUID();
    private Device mockActiveDevice;
    private Device mockInactiveDevice;

    @BeforeEach
    void setUp() {
        // Setup mock domain objects (must use the all-args constructor)
        mockActiveDevice = new Device(
                UUID.randomUUID(),
                "ActivePhone",
                "BrandA",
                DeviceState.AVAILABLE,
                Instant.now()
        );
        mockInactiveDevice = new Device(
                TEST_ID,
                "OldTablet",
                "BrandB",
                DeviceState.INACTIVE,
                Instant.now()
        );
    }

    // --- 1. Test getAll ---

    @Test
    void getAll_ShouldReturnAllDevicesMapped() {
        // Arrange
        List<Device> domainList = List.of(mockActiveDevice, mockInactiveDevice);
        when(repository.getAllDevices()).thenReturn(domainList);

        // Act
        List<DeviceUseCaseResponse> result = getDeviceService.getAll();

        // Assert
        verify(repository, times(1)).getAllDevices();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(mockActiveDevice.getName(), result.get(0).getName(), "The first device name should be mapped.");
    }

    @Test
    void getAll_ShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        // Arrange
        when(repository.getAllDevices()).thenReturn(Collections.emptyList());

        // Act
        List<DeviceUseCaseResponse> result = getDeviceService.getAll();

        // Assert
        verify(repository, times(1)).getAllDevices();
        assertTrue(result.isEmpty());
    }

    // --- 2. Test findById ---

    @Test
    void findById_ShouldReturnDeviceWhenFound() {
        // Arrange
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(mockInactiveDevice));

        // Act
        DeviceUseCaseResponse result = getDeviceService.findById(TEST_ID);

        // Assert
        verify(repository, times(1)).findById(TEST_ID);
        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(DeviceState.INACTIVE, result.getState());
    }

    @Test
    void findById_ShouldThrowNotFoundExceptionWhenEmpty() {
        // Arrange
        when(repository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        DeviceNotFoundException exception = assertThrows(
                DeviceNotFoundException.class,
                () -> getDeviceService.findById(TEST_ID)
        );

        assertTrue(exception.getMessage().contains(TEST_ID.toString()));
        verify(repository, times(1)).findById(TEST_ID);
    }

    // --- 3. Test getByBrand ---

    @Test
    void getByBrand_ShouldReturnFilteredListMapped() {
        // Arrange
        String brand = "BrandA";
        List<Device> domainList = List.of(mockActiveDevice);
        when(repository.findByBrand(brand)).thenReturn(domainList);

        // Act
        List<DeviceUseCaseResponse> result = getDeviceService.getByBrand(brand);

        // Assert
        verify(repository, times(1)).findByBrand(brand);
        assertEquals(1, result.size());
        assertEquals(brand, result.get(0).getBrand());
    }

    // --- 4. Test getByState ---

    @Test
    void getByState_ShouldReturnFilteredListMapped() {
        // Arrange
        DeviceState state = DeviceState.AVAILABLE;
        List<Device> domainList = List.of(mockActiveDevice);
        when(repository.findByState(state)).thenReturn(domainList);

        // Act
        List<DeviceUseCaseResponse> result = getDeviceService.getByState(state);

        // Assert
        verify(repository, times(1)).findByState(state);
        assertEquals(1, result.size());
        assertEquals(state, result.get(0).getState());
    }
}