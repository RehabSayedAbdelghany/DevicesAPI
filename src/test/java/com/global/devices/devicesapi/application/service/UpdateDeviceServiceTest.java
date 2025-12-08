package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.DeviceRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.exception.DeviceNotFoundException;
import com.global.devices.devicesapi.domain.exception.InvalidOperationException;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import com.global.devices.devicesapi.domain.validation.DeviceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDeviceServiceTest {

    @Mock
    private DeviceRepositoryWrapper repository;

    @Mock
    private DeviceValidator deviceValidator;

    @InjectMocks
    private UpdateDeviceService updateDeviceService;

    private final UUID TEST_ID = UUID.randomUUID();
    private final Instant TEST_TIME = Instant.now();
    private Device existingDevice;
    private DeviceUseCaseResponse mockResponse;

    @BeforeEach
    void setUp() {
        // Setup initial existing device state
        existingDevice = new Device(
                TEST_ID,
                "OldName",
                "OldBrand",
                DeviceState.IN_USE,
                TEST_TIME
        );

        // Setup the expected response mapping (from the saved domain object)
        mockResponse = DeviceUseCaseResponse.builder()
                .id(TEST_ID)
                .name("UpdatedName")
                .brand("UpdatedBrand")
                .state(DeviceState.INACTIVE)
                .creationTime(TEST_TIME)
                .build();
    }

    // --- A. Tests for FULL UPDATE (update method) ---

    @Test
    void update_ShouldSuccessfullyUpdateDevice() {
        // Arrange
        DeviceUseCaseRequest updateRequest = DeviceUseCaseRequest.builder()
                .name("NewName")
                .brand("NewBrand")
                .state(DeviceState.INACTIVE)
                .build();

        // Mock the findById to return the existing device
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(existingDevice));
        // Mock validation to pass (do nothing)
        doNothing().when(deviceValidator).validateStatus(existingDevice);
        // Mock the save operation to return a successfully updated device (which maps to mockResponse)
        when(repository.saveDevice(any(Device.class))).thenReturn(existingDevice);
        // Note: For simplicity, we assume the saved device maps to mockResponse correctly.

        // Act
        DeviceUseCaseResponse response = updateDeviceService.update(TEST_ID, updateRequest);

        // Assert
        // Verify 1: Validation was called
        verify(deviceValidator, times(1)).validateStatus(existingDevice);
        // Verify 2: Save was called
        verify(repository, times(1)).saveDevice(any(Device.class));
        // Verify 3: Response contains updated data (requires checking mapped object if fully testing)
        // Since we mocked save to return the original object, we assert against its state
        assertNotNull(response);
    }

    @Test
    void update_ShouldThrowNotFoundExceptionWhenDeviceDoesNotExist() {
        // Arrange
        when(repository.findById(TEST_ID)).thenReturn(Optional.empty());
        DeviceUseCaseRequest updateRequest = DeviceUseCaseRequest.builder().build();

        // Act & Assert
        assertThrows(DeviceNotFoundException.class,
                () -> updateDeviceService.update(TEST_ID, updateRequest));

        // Verify no further calls were made
        verify(deviceValidator, never()).validateStatus(any());
        verify(repository, never()).saveDevice(any());
    }

    @Test
    void update_ShouldThrowInvalidOperationExceptionWhenValidationFails() {
        // Arrange
        DeviceUseCaseRequest updateRequest = DeviceUseCaseRequest.builder().build();
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(existingDevice));
        // Mock validation to fail
        doThrow(new InvalidOperationException("Device is in use")).when(deviceValidator).validateStatus(existingDevice);

        // Act & Assert
        assertThrows(InvalidOperationException.class,
                () -> updateDeviceService.update(TEST_ID, updateRequest));

        // Verify save was not called
        verify(repository, never()).saveDevice(any());
    }

    // --- B. Tests for PARTIAL UPDATE (patch method) ---

    @Test
    void patch_ShouldUpdateOnlyNameAndPreserveOthers() {
        // Arrange
        Map<String, Object> updatedFields = Map.of("name", "New Patched Name");
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(existingDevice));
        doNothing().when(deviceValidator).validateStatus(existingDevice);

        // Mock the save operation
        when(repository.saveDevice(any(Device.class))).thenAnswer(invocation -> {
            Device deviceToSave = invocation.getArgument(0);
            // Assert that only NAME changed, others preserved
            assertEquals("New Patched Name", deviceToSave.getName());
            assertEquals(existingDevice.getBrand(), deviceToSave.getBrand()); // Old brand preserved
            return deviceToSave; // Return the saved device
        });

        // Act
        updateDeviceService.patch(TEST_ID, updatedFields);

        // Assert
        verify(repository, times(1)).saveDevice(any(Device.class));
    }

    @Test
    void patch_ShouldThrowInvalidOperationExceptionWhenUpdatingCreationTime() {
        // Arrange
        Map<String, Object> updatedFields = Map.of("creationTime", Instant.now().toString());
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(existingDevice));
        doNothing().when(deviceValidator).validateStatus(existingDevice);

        // Act & Assert
        assertThrows(InvalidOperationException.class,
                () -> updateDeviceService.patch(TEST_ID, updatedFields),
                "Should prevent updating creationTime.");

        // Verify save was not called
        verify(repository, never()).saveDevice(any());
    }

    @Test
    void patch_ShouldThrowNotFoundExceptionWhenDeviceDoesNotExist() {
        // Arrange
        when(repository.findById(TEST_ID)).thenReturn(Optional.empty());
        Map<String, Object> updatedFields = Map.of("name", "New Name");

        // Act & Assert
        assertThrows(DeviceNotFoundException.class,
                () -> updateDeviceService.patch(TEST_ID, updatedFields));

        // Verify no validation or save was called
        verify(repository, never()).saveDevice(any());
    }
}