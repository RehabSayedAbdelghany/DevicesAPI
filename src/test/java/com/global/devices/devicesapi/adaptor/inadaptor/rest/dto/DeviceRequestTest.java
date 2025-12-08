package com.global.devices.devicesapi.adaptor.inadaptor.rest.dto;

import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.domain.model.DeviceState;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeviceRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        DeviceRequest request = DeviceRequest.builder()
                .name("Smartphone X")
                .brand("Acme")
                .state(DeviceState.IN_USE)
                .build();

        Set<ConstraintViolation<DeviceRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Expected zero violations for a valid object.");
    }

    @Test
    void shouldFailValidation_WhenNameIsMissing() {
        DeviceRequest request = DeviceRequest.builder()
                .brand("Acme")
                .state(DeviceState.IN_USE)
                .build();

        Set<ConstraintViolation<DeviceRequest>> violations = validator.validate(request);

        // Expect one error due to @NotBlank on 'name'
        assertFalse(violations.isEmpty(), "Expected validation failure for missing name.");
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenBrandIsBlank() {
        DeviceRequest request = DeviceRequest.builder()
                .name("Laptop Z")
                .brand("")
                .state(DeviceState.INACTIVE)
                .build();

        Set<ConstraintViolation<DeviceRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation failure for blank brand.");
        assertEquals("Brand is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenStateIsNull() {
        DeviceRequest request = DeviceRequest.builder()
                .name("Tablet Y")
                .brand("Beta")
                .state(null)
                .build();

        Set<ConstraintViolation<DeviceRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Expected validation failure for null state.");
        assertEquals("state is required", violations.iterator().next().getMessage());
    }


    @Test
    void toUseCaseRequest_ShouldMapAllFieldsCorrectly() {
        // Arrange
        DeviceRequest request = DeviceRequest.builder()
                .name("Server Alpha")
                .brand("Gamma Corp")
                .state(DeviceState.IN_USE)
                .build();

        // Act
        DeviceUseCaseRequest useCaseRequest = request.toUseCaseRequest();

        // Assert
        assertNotNull(useCaseRequest, "Mapped object should not be null.");
        assertEquals("Server Alpha", useCaseRequest.getName(), "Name should be mapped.");
        assertEquals("Gamma Corp", useCaseRequest.getBrand(), "Brand should be mapped.");
        assertEquals(DeviceState.IN_USE, useCaseRequest.getState(), "State should be mapped.");
    }
}