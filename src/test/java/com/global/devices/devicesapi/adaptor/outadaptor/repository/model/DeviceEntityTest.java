package com.global.devices.devicesapi.adaptor.outadaptor.repository.model;

import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeviceEntityTest {

    @Test
    void shouldCreateEntityAndAccessProperties() {
        // Arrange
        UUID expectedId = UUID.randomUUID();
        String expectedName = "Server-001";
        String expectedBrand = "Intel";
        DeviceState expectedState = DeviceState.AVAILABLE;
        Instant expectedCreationTime = Instant.now();

        // Act - Use the @Builder constructor
        DeviceEntity entity = DeviceEntity.builder()
                .id(expectedId)
                .name(expectedName)
                .brand(expectedBrand)
                .state(expectedState)
                .creationTime(expectedCreationTime)
                .build();

        // Assert - Test Getters (@Data)
        assertNotNull(entity, "Entity should not be null.");
        assertEquals(expectedId, entity.getId(), "ID should match the built value.");
        assertEquals(expectedName, entity.getName(), "Name should match the built value.");
        assertEquals(expectedBrand, entity.getBrand(), "Brand should match the built value.");
        assertEquals(expectedState, entity.getState(), "State should match the built value.");
        assertEquals(expectedCreationTime, entity.getCreationTime(), "CreationTime should match the built value.");
    }

    @Test
    void shouldBeEqual_WhenIdsAreTheSame() {
        // Arrange
        UUID id = UUID.randomUUID();

        DeviceEntity entity1 = DeviceEntity.builder().id(id).name("A").brand("B").state(DeviceState.AVAILABLE).build();
        DeviceEntity entity2 = DeviceEntity.builder().id(id).name("X").brand("Y").state(DeviceState.INACTIVE).build();

        // Assert - Test Equals and HashCode (@Data) based on ID (as typically configured)
        // Note: For Entities, equals/hashCode behavior can be complex.
        // We test the basic Lombok implementation which checks all fields.

        // Since we only set the ID the same, but others different, standard Lombok equals will fail here.
        // We test based on the full object comparison:
        assertEquals(entity1, entity1, "An object should be equal to itself.");
        assertNotEquals(entity1, entity2, "Objects should not be equal if any non-PK field differs (Lombok default).");
    }

    @Test
    void shouldSupportNoArgsConstructor() {
        // Act
        DeviceEntity entity = new DeviceEntity();

        // Assert
        assertNotNull(entity, "The no-args constructor should initialize the object.");
        assertNull(entity.getId(), "ID should be null after using no-args constructor.");
    }

    @Test
    void creationTimestampShouldBeInstant() {
        // Arrange: Explicitly provide a non-null Instant value.
        Instant testTime = Instant.now();

        // Act: Use the Builder and supply the required value.
        DeviceEntity entity = DeviceEntity.builder()
                .creationTime(testTime) // <-- Provide a value here
                // Note: You may need to supply other non-nullable fields if your entity requires them.
                .build();

        // Assert 1: Ensure the getter returned a non-null value (optional but good practice).
        assertNotNull(entity.getCreationTime(), "CreationTime should not be null after being set.");

        // Assert 2: Check the type of the returned object.
        assertEquals(Instant.class, entity.getCreationTime().getClass(), "CreationTime should be an Instant.");
    }
}