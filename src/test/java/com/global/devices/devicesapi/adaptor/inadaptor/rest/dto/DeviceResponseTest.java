package com.global.devices.devicesapi.adaptor.inadaptor.rest.dto;

import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeviceResponseTest {

    @Test
    void from_ShouldMapAllFieldsFromUseCaseResponseCorrectly() {
        // Arrange
        UUID expectedId = UUID.randomUUID();
        String expectedName = "Smart Lamp";
        String expectedBrand = "Luminos";
        DeviceState expectedState = DeviceState.AVAILABLE;
        Instant expectedCreationTime = Instant.now();

        // Create the source object (DeviceUseCaseResponse)
        DeviceUseCaseResponse useCaseResponse = DeviceUseCaseResponse.builder()
                .id(expectedId)
                .name(expectedName)
                .brand(expectedBrand)
                .state(expectedState)
                .creationTime(expectedCreationTime)
                .build();

        // Act
        DeviceResponse responseDto = DeviceResponse.from(useCaseResponse);


        assertNotNull(responseDto, "The mapped DTO should not be null.");


        assertEquals(expectedId, responseDto.getId(), "ID field must be mapped correctly.");
        assertEquals(expectedName, responseDto.getName(), "Name field must be mapped correctly.");
        assertEquals(expectedBrand, responseDto.getBrand(), "Brand field must be mapped correctly.");
        assertEquals(expectedState, responseDto.getState(), "State field must be mapped correctly.");
        assertEquals(expectedCreationTime, responseDto.getCreationTime(), "CreationTime field must be mapped correctly.");
    }

}