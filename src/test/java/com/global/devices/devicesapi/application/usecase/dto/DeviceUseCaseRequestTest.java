package com.global.devices.devicesapi.application.usecase.dto;

import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeviceUseCaseRequestTest {

    @Test
    void shouldCreateObjectWithBuilderAndAccessFields() {
        // Arrange
        String name = "Test Device";
        String brand = "Test Brand";
        DeviceState state = DeviceState.AVAILABLE;

        // Act: Use the @Builder constructor
        DeviceUseCaseRequest request = DeviceUseCaseRequest.builder()
                .name(name)
                .brand(brand)
                .state(state)
                .build();

        // Assert: Verify getters (@Getter)
        assertNotNull(request, "Request object should not be null.");
        assertEquals(name, request.getName(), "Name should be retrieved correctly.");
        assertEquals(brand, request.getBrand(), "Brand should be retrieved correctly.");
        assertEquals(state, request.getState(), "State should be retrieved correctly.");
    }

    @Test
    void shouldCreateObjectWithNoArgsConstructor() {
        // Act: Use the @NoArgsConstructor
        DeviceUseCaseRequest request = new DeviceUseCaseRequest();

        // Assert
        assertNotNull(request, "No-args constructor should initialize the object.");
        assertNull(request.getName(), "Fields should be null after no-args construction.");
    }

    @Test
    void shouldCreateObjectWithAllArgsConstructor() {
        // Arrange
        String name = "Full Device";
        String brand = "Full Brand";
        DeviceState state = DeviceState.INACTIVE;

        // Act: Use the @AllArgsConstructor
        DeviceUseCaseRequest request = new DeviceUseCaseRequest(name, brand, state);

        // Assert
        assertEquals(name, request.getName());
        assertEquals(state, request.getState());
    }

    @Test
    void shouldSetNewValuesUsingSetters() {
        // Arrange
        DeviceUseCaseRequest request = new DeviceUseCaseRequest();
        String newName = "New Name";
        DeviceState newState = DeviceState.AVAILABLE;

        // Act: Use the generated setters (@Setter)
        request.setName(newName);
        request.setState(newState);

        // Assert
        assertEquals(newName, request.getName(), "Name should be updated via setter.");
        assertEquals(newState, request.getState(), "State should be updated via setter.");
    }
}