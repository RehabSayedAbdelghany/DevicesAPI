package com.global.devices.devicesapi.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidOperationExceptionTest {

    @Test
    void constructor_ShouldCreateExceptionWithCorrectMessage() {
        // Arrange
        String expectedMessage = "Cannot delete device: it is currently IN_USE.";

        // Act
        InvalidOperationException exception = new InvalidOperationException(expectedMessage);

        // Assert
        assertNotNull(exception, "Exception instance should not be null.");

        // 1. Verify the message is stored correctly (inherited from RuntimeException)
        assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should match the one provided in the constructor.");

        // 2. Verify the exception type
        assertTrue(exception instanceof RuntimeException,
                "InvalidOperationException must be a RuntimeException.");

        // 3. Verify cause is null (as no nested exception was provided)
        assertNull(exception.getCause(), "Cause should be null.");
    }

    @Test
    void constructor_ShouldAllowNullMessage() {
        // Act
        InvalidOperationException exception = new InvalidOperationException(null);

        // Assert
        assertNull(exception.getMessage(), "The message should be null if null was provided.");
    }
}