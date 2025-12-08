package com.global.devices.devicesapi.domain.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private final int TEST_STATUS = 404;
    private final String TEST_ERROR = "Not Found";
    private final String TEST_MESSAGE = "Resource does not exist.";
    private final String TEST_PATH = "/api/v1/devices/123";

    // --- 1. Test Static Factory Method (of) ---

    @Test
    void of_ShouldInitializeAllFieldsCorrectly() {
        // Act
        ErrorResponse response = ErrorResponse.of(TEST_STATUS, TEST_ERROR, TEST_MESSAGE, TEST_PATH);

        // Assert
        assertNotNull(response, "Response object should not be null.");
        assertNotNull(response.timestamp(), "Timestamp must be generated and not null.");

        assertEquals(TEST_STATUS, response.status(), "Status should be set correctly.");
        assertEquals(TEST_ERROR, response.error(), "Error name should be set correctly.");
        assertEquals(TEST_MESSAGE, response.message(), "Message should be set correctly.");
        assertEquals(TEST_PATH, response.path(), "Path should be set correctly.");
    }

    // --- 2. Test Record Immutability and Accessors ---

    @Test
    void record_AccessorsShouldReturnCorrectValues() {
        // Arrange: Directly instantiate the record (using canonical constructor)
        LocalDateTime specificTime = LocalDateTime.of(2025, 12, 10, 10, 30, 0);
        ErrorResponse response = new ErrorResponse(specificTime, TEST_STATUS, TEST_ERROR, TEST_MESSAGE, TEST_PATH);

        // Assert: Verify built-in accessors
        assertEquals(specificTime, response.timestamp());
        assertEquals(TEST_STATUS, response.status());
        assertEquals(TEST_MESSAGE, response.message());
    }

    // --- 3. Test Record Equality (equals() and hashCode()) ---

    @Test
    void record_ShouldBeEqual_WhenAllFieldsAreIdentical() {
        // Arrange
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 12, 0, 0);

        ErrorResponse response1 = new ErrorResponse(time, 400, "Bad Request", "Message A", "/path");
        ErrorResponse response2 = new ErrorResponse(time, 400, "Bad Request", "Message A", "/path");

        // Assert
        assertTrue(response1.equals(response2), "Identical records must be equal.");
        assertEquals(response1.hashCode(), response2.hashCode(), "Hash codes must be equal for identical records.");
    }

    @Test
    void record_ShouldNotBeEqual_WhenStatusDiffers() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();

        ErrorResponse response1 = new ErrorResponse(time, 400, "Error", "Msg", "/path");
        ErrorResponse response2 = new ErrorResponse(time, 500, "Error", "Msg", "/path");

        // Assert
        assertFalse(response1.equals(response2), "Records with different status codes should not be equal.");
    }
}