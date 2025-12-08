package com.global.devices.devicesapi.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeviceNotFoundExceptionTest {

    @Test
    void constructor_ShouldCreateExceptionWithCorrectMessage() {
        // Arrange
        String expectedMessage = "Device with ID 1234 not found in the repository.";

        // Act
        DeviceNotFoundException exception = new DeviceNotFoundException(expectedMessage);

        // Assert
        assertNotNull(exception, "Exception instance should not be null.");

        // 1. Verify the message is stored correctly (inherited from RuntimeException)
        assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should match the one provided in the constructor.");

        // 2. Verify the exception type
        assertTrue(exception instanceof RuntimeException,
                "DeviceNotFoundException must be a RuntimeException.");

        // 3. Verify cause is null (as no nested exception was provided)
        assertNull(exception.getCause(), "Cause should be null.");
    }

    @Test
    void constructor_ShouldAllowNullMessage() {
        // Act
        DeviceNotFoundException exception = new DeviceNotFoundException(null);

        // Assert
        assertNull(exception.getMessage(), "The message should be null if null was provided.");
    }
}