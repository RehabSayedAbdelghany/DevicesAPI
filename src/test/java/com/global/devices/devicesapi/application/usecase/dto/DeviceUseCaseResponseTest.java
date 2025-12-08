package com.global.devices.devicesapi.application.usecase.dto;

import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeviceUseCaseResponseTest {

    private final UUID TEST_ID = UUID.randomUUID();
    private final String TEST_NAME = "SmartPlug";
    private final Instant TEST_TIME = Instant.now();

    @Test
    void shouldCreateObjectWithBuilderAndAccessFields() {
        // Arrange
        DeviceState state = DeviceState.AVAILABLE;

        // Act: Use the @Builder constructor
        DeviceUseCaseResponse response = DeviceUseCaseResponse.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .brand("TechBrand")
                .state(state)
                .creationTime(TEST_TIME)
                .build();

        // Assert: Verify getters (@Data / @Getter)
        assertNotNull(response, "Response object should not be null.");
        assertEquals(TEST_ID, response.getId(), "ID should be retrieved correctly.");
        assertEquals(TEST_NAME, response.getName(), "Name should be retrieved correctly.");
        assertEquals(state, response.getState(), "State should be retrieved correctly.");
        assertEquals(TEST_TIME, response.getCreationTime(), "CreationTime should be retrieved correctly.");
    }

    @Test
    void shouldSupportNoArgsConstructor() {
        // Act: Use the @NoArgsConstructor
        DeviceUseCaseResponse response = new DeviceUseCaseResponse();

        // Assert
        assertNotNull(response, "No-args constructor should initialize the object.");
        assertNull(response.getId(), "Fields should be null after no-args construction.");
    }

    @Test
    void shouldSetNewValuesUsingSetters() {
        // Arrange
        DeviceUseCaseResponse response = new DeviceUseCaseResponse();
        String newBrand = "NewBrand";
        UUID newId = UUID.randomUUID();

        // Act: Use the generated setters (@Setter)
        response.setId(newId);
        response.setBrand(newBrand);

        // Assert
        assertEquals(newId, response.getId(), "ID should be updated via setter.");
        assertEquals(newBrand, response.getBrand(), "Brand should be updated via setter.");
    }

    @Test
    void shouldBeEqual_WhenAllFieldsAreSame() {
        // Arrange
        DeviceUseCaseResponse response1 = DeviceUseCaseResponse.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .brand("A")
                .state(DeviceState.AVAILABLE)
                .creationTime(TEST_TIME)
                .build();

        DeviceUseCaseResponse response2 = DeviceUseCaseResponse.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .brand("A")
                .state(DeviceState.AVAILABLE)
                .creationTime(TEST_TIME)
                .build();

        // Assert: Test Equals and HashCode (@Data)
        assertEquals(response1, response2, "Objects with identical fields should be equal.");
        assertEquals(response1.hashCode(), response2.hashCode(), "Hash codes should be equal.");
    }
}