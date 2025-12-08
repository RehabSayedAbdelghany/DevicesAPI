package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.DeviceRepositoryWrapper;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDeviceServiceTest {

    // Mock the data access dependency
    @Mock
    private DeviceRepositoryWrapper repository;

    // Mock the business rule validation dependency
    @Mock
    private DeviceValidator deviceValidator;

    // Inject the mocks into the class being tested
    @InjectMocks
    private DeleteDeviceService deleteDeviceService;

    private final UUID TEST_ID = UUID.randomUUID();
    private Device activeDevice;

    @BeforeEach
    void setUp() {
        // Setup a mock Device object that will be returned when found
        activeDevice = new Device(
                TEST_ID,
                "Device to Delete",
                "Brand",
                DeviceState.AVAILABLE,
                Instant.now()
        );
    }

    // --- 1. Test Successful Deletion (Happy Path) ---

    @Test
    void deleteDevice_ShouldFindValidateAndDeleteWhenSuccessful() {
        // Arrange
        // 1. Repository finds the device
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(activeDevice));
        // 2. Validator passes (i.e., does nothing/doesn't throw)
        doNothing().when(deviceValidator).validateStatus(activeDevice);
        // 3. Repository delete call is observed

        // Act
        deleteDeviceService.deleteDevice(TEST_ID);

        // Assert
        // Verify 1: Device was found
        verify(repository, times(1)).findById(TEST_ID);
        // Verify 2: Status validation was performed on the retrieved device
        verify(deviceValidator, times(1)).validateStatus(activeDevice);
        // Verify 3: The delete operation was called on the repository
        verify(repository, times(1)).deleteDeviceById(TEST_ID);
    }

    // --- 2. Test Device Not Found Scenario ---

    @Test
    void deleteDevice_ShouldThrowDeviceNotFoundException() {
        // Arrange
        // Repository returns an empty Optional
        when(repository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        DeviceNotFoundException exception = assertThrows(
                DeviceNotFoundException.class,
                () -> deleteDeviceService.deleteDevice(TEST_ID),
                "Expected DeviceNotFoundException to be thrown."
        );

        // Assert 2: Check the exception message
        assertTrue(exception.getMessage().contains("Device with id " + TEST_ID + " not found"));

        // Assert 3: Verify NO validation or deletion was attempted
        verify(deviceValidator, never()).validateStatus(any(Device.class));
        verify(repository, never()).deleteDeviceById(any(UUID.class));
    }

    // --- 3. Test Validation Failure Scenario ---

    @Test
    void deleteDevice_ShouldThrowInvalidOperationExceptionWhenValidationFails() {
        // Arrange
        // 1. Repository finds the device
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(activeDevice));
        // 2. Validator throws a business exception (e.g., if the state is IN_USE)
        doThrow(new InvalidOperationException("In-use devices cannot be deleted"))
                .when(deviceValidator).validateStatus(activeDevice);

        // Act & Assert
        assertThrows(
                InvalidOperationException.class,
                () -> deleteDeviceService.deleteDevice(TEST_ID),
                "Expected InvalidOperationException to be thrown when validation fails."
        );

        // Assert 2: Verify validation was attempted but deletion was NOT called
        verify(deviceValidator, times(1)).validateStatus(activeDevice);
        verify(repository, never()).deleteDeviceById(any(UUID.class));
    }
}