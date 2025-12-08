package com.global.devices.devicesapi.domain.validation;

import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeviceValidatorTest {

    private DeviceValidator deviceValidator;
    private final UUID TEST_ID = UUID.randomUUID();
    private final Instant TEST_TIME = Instant.now();

    @BeforeEach
    void setUp() {
        // Instantiate the class being tested
        deviceValidator = new DeviceValidator();
    }

    // --- 1. Test Successful Validation (Operation Allowed) ---

    @Test
    void validateStatus_ShouldNotThrowException_WhenDeviceIsActive() {
        // Arrange
        Device activeDevice = new Device(
                TEST_ID, "Test", "Brand", DeviceState.AVAILABLE, TEST_TIME
        );

        // Act & Assert
        // The test passes if the method executes without throwing any exception
        assertDoesNotThrow(() -> deviceValidator.validateStatus(activeDevice),
                "Validation should pass for an ACTIVE device.");
    }

    @Test
    void validateStatus_ShouldNotThrowException_WhenDeviceIsInactive() {
        // Arrange
        Device inactiveDevice = new Device(
                TEST_ID, "Test", "Brand", DeviceState.INACTIVE, TEST_TIME
        );

        // Act & Assert
        assertDoesNotThrow(() -> deviceValidator.validateStatus(inactiveDevice),
                "Validation should pass for an INACTIVE device.");
    }




}