package com.global.devices.devicesapi.domain.model;

import com.global.devices.devicesapi.adaptor.outadaptor.repository.model.DeviceEntity;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    private final String TEST_NAME = "Smart Watch";
    private final String TEST_BRAND = "TimeCo";
    private final DeviceState TEST_STATE = DeviceState.AVAILABLE;
    private final UUID TEST_ID = UUID.randomUUID();
    private final Instant TEST_TIME = Instant.now();

    // --- 1. Test Constructor for New Device Creation ---

    @Test
    void constructor_ShouldInitializeNewDeviceCorrectly() {
        // Act: Use the constructor that generates ID and creationTime
        Device newDevice = new Device(TEST_NAME, TEST_BRAND, TEST_STATE);

        // Assert: Verify auto-generated fields are present and inputs are correct
        assertNotNull(newDevice.getId(), "ID must be generated.");
        assertNotNull(newDevice.getCreationTime(), "CreationTime must be generated.");
        assertEquals(TEST_NAME, newDevice.getName());
        assertEquals(TEST_BRAND, newDevice.getBrand());
        assertEquals(TEST_STATE, newDevice.getState());
    }

    // --- 2. Test Constructor for Hydrating (Loading) Device ---

    @Test
    void constructor_ShouldInitializeLoadedDeviceCorrectly() {
        // Act: Use the constructor that accepts all fields (for loading from DB)
        Device loadedDevice = new Device(TEST_ID, TEST_NAME, TEST_BRAND, TEST_STATE, TEST_TIME);

        // Assert: Verify all fields match the loaded input
        assertEquals(TEST_ID, loadedDevice.getId());
        assertEquals(TEST_NAME, loadedDevice.getName());
        assertEquals(TEST_BRAND, loadedDevice.getBrand());
        assertEquals(TEST_STATE, loadedDevice.getState());
        assertEquals(TEST_TIME, loadedDevice.getCreationTime());
    }

    // --- 3. Test Mapping to Use Case Response ---

    @Test
    void toUseCaseResponse_ShouldMapAllFieldsCorrectly() {
        // Arrange
        Device device = new Device(TEST_ID, TEST_NAME, TEST_BRAND, TEST_STATE, TEST_TIME);

        // Act
        DeviceUseCaseResponse response = device.toUseCaseResponse();

        // Assert: Check data transfer to the DTO
        assertNotNull(response);
        assertEquals(TEST_ID, response.getId());
        assertEquals(TEST_NAME, response.getName());
        assertEquals(TEST_BRAND, response.getBrand());
        assertEquals(TEST_STATE, response.getState());
        assertEquals(TEST_TIME, response.getCreationTime());
    }

    // --- 4. Test Mapping to Persistence Entity ---

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        // Arrange
        Device device = new Device(TEST_ID, TEST_NAME, TEST_BRAND, TEST_STATE, TEST_TIME);

        // Act
        DeviceEntity entity = device.toEntity();

        // Assert: Check data transfer to the persistence object
        assertNotNull(entity);
        assertEquals(TEST_ID, entity.getId());
        assertEquals(TEST_NAME, entity.getName());
        assertEquals(TEST_BRAND, entity.getBrand());
        assertEquals(TEST_STATE, entity.getState());
        assertEquals(TEST_TIME, entity.getCreationTime());
    }
}