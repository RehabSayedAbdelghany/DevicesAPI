package com.global.devices.devicesapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.devices.devicesapi.adaptor.inadaptor.rest.DeviceController;
import com.global.devices.devicesapi.adaptor.inadaptor.rest.dto.DeviceRequest;
import com.global.devices.devicesapi.application.usecase.CreateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.DeleteDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.GetDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.UpdateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.exception.DeviceNotFoundException;
import com.global.devices.devicesapi.domain.exception.InvalidOperationException;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Use WebMvcTest to test the controller layer in isolation
// It auto-configures Spring MVC and provides MockMvc
@WebMvcTest(DeviceController.class)
public class DeviceControllerIntegrationTest {

    private static final String BASE_URL = "/devices-api/v1/devices";
    private final UUID DEVICE_ID = UUID.fromString("1a2b3c4d-5e6f-7080-90a0-b0c0d0e0f000");
    private final UUID NON_EXISTENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock the Use Case dependencies
    @MockBean
    private CreateDeviceUseCase createDeviceUseCase;
    @MockBean
    private GetDeviceUseCase getDeviceUseCase;
    @MockBean
    private DeleteDeviceUseCase deleteDeviceUseCase;
    @MockBean
    private UpdateDeviceUseCase updateDeviceUseCase;

    // Helper method to create a valid DeviceRequest
    private DeviceRequest createValidDeviceRequest() {
        return new DeviceRequest("Laptop", "Dell", DeviceState.AVAILABLE);
    }

    // Helper method to create a valid DeviceUseCaseResponse
    private DeviceUseCaseResponse createValidDeviceResponse() {
        return new DeviceUseCaseResponse(
                DEVICE_ID,
                "Laptop",
                "Dell",
                DeviceState.AVAILABLE,
                Instant.now()
        );
    }

    // --- POST /devices ---
    @Test
    void createDevice_shouldReturn201AndDeviceResponse_whenSuccessful() throws Exception {
        // Arrange
        DeviceRequest request = createValidDeviceRequest();
        DeviceUseCaseResponse expectedResponse = createValidDeviceResponse();

        when(createDeviceUseCase.create(any(DeviceUseCaseRequest.class))).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verify the response content based on the UseCaseResponse
                .andExpect(jsonPath("$.id").value(DEVICE_ID.toString()))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.brand").value("Dell"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"));

        // Verify the Use Case was called
        verify(createDeviceUseCase, times(1)).create(any(DeviceUseCaseRequest.class));
    }


    @Test
    void createDevice_shouldReturn2400_fail() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
{
    "name": "name3",
    "brand": "brand3"
}
"""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verify the error structure returned by Spring for validation failure
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed")) // Adjust 'error' value if different in your app
                // Check for the specific error details, matching your provided JSON body structure
                .andExpect(jsonPath("$.errors.state").exists());

        // Verify the Use Case was called
        verify(createDeviceUseCase, times(0)).create(any(DeviceUseCaseRequest.class));
    }


    // --- GET /devices/{id} ---
    @Test
    void getDeviceById_shouldReturn200AndDeviceResponse_whenFound() throws Exception {
        // Arrange
        DeviceUseCaseResponse expectedResponse = createValidDeviceResponse();

        when(getDeviceUseCase.findById(eq(DEVICE_ID))).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}", DEVICE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEVICE_ID.toString()));

        verify(getDeviceUseCase, times(1)).findById(eq(DEVICE_ID));
    }

    @Test
    void getDeviceById_shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        doThrow(new DeviceNotFoundException("Device not found")).when(getDeviceUseCase).findById(eq(NON_EXISTENT_ID));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}", NON_EXISTENT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Assuming Spring's default exception handling maps custom exceptions
        // to appropriate status codes, or a global handler is present.

        verify(getDeviceUseCase, times(1)).findById(eq(NON_EXISTENT_ID));
    }



    // --- GET /devices ---
    @Test
    void getDevices_shouldReturn200AndListOfDevices_whenFound() throws Exception {
        // Arrange
        DeviceUseCaseResponse device1 = createValidDeviceResponse();
        DeviceUseCaseResponse device2 = new DeviceUseCaseResponse(
                UUID.randomUUID(), "Desktop", "HP",DeviceState.IN_USE, Instant.now());

        List<DeviceUseCaseResponse> deviceList = List.of(device1, device2);

        when(getDeviceUseCase.getAll()).thenReturn(deviceList);

        // Act & Assert
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Desktop"));

        verify(getDeviceUseCase, times(1)).getAll();
    }

    @Test
    void getDevices_shouldReturn200AndEmptyList_whenNoneFound() throws Exception {
        // Arrange
        when(getDeviceUseCase.getAll()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(getDeviceUseCase, times(1)).getAll();
    }

    // --- GET /devices?brand={brand} ---
    @Test
    void getByBrand_shouldReturn200AndFilteredList() throws Exception {
        // Arrange
        String brand = "Dell";
        DeviceUseCaseResponse device1 = createValidDeviceResponse();
        List<DeviceUseCaseResponse> filteredList = List.of(device1);

        when(getDeviceUseCase.getByBrand(eq(brand))).thenReturn(filteredList);

        // Act & Assert
        mockMvc.perform(get(BASE_URL)
                        .param("brand", brand)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].brand").value(brand));

        verify(getDeviceUseCase, times(1)).getByBrand(eq(brand));
    }

    // --- GET /devices?state={state} ---
    @Test
    void getByState_shouldReturn200AndFilteredList() throws Exception {
        // Arrange
        String state = "AVAILABLE";
        DeviceUseCaseResponse device1 = createValidDeviceResponse();
        List<DeviceUseCaseResponse> filteredList = List.of(device1);

        when(getDeviceUseCase.getByState(eq(DeviceState.AVAILABLE))).thenReturn(filteredList);

        // Act & Assert
        mockMvc.perform(get(BASE_URL)
                        .param("state", state)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].state").value(state));

        verify(getDeviceUseCase, times(1)).getByState(eq(DeviceState.AVAILABLE));
    }

    // --- PUT /devices/{id} ---
    @Test
    void updateDevice_shouldReturn201AndUpdatedDeviceResponse_whenSuccessful() throws Exception {
        // Arrange
        DeviceRequest request = new DeviceRequest("Updated Laptop", "HP",  DeviceState.IN_USE);
        DeviceUseCaseResponse updatedResponse = new DeviceUseCaseResponse(
                DEVICE_ID, "Updated Laptop", "HP", DeviceState.IN_USE, Instant.now());

        when(updateDeviceUseCase.update(eq(DEVICE_ID), any(DeviceUseCaseRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/{id}", DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.brand").value("HP"))
                .andExpect(jsonPath("$.state").value("IN_USE"));

        verify(updateDeviceUseCase, times(1)).update(eq(DEVICE_ID), any(DeviceUseCaseRequest.class));
    }

    // --- PATCH /devices/{id} ---
    @Test
    void patchDevice_shouldReturn201AndPatchedDeviceResponse_whenSuccessful() throws Exception {
        // Arrange
        Map<String, Object> patchFields = Map.of("state", "IN_USE");
        DeviceUseCaseResponse patchedResponse = new DeviceUseCaseResponse(
                DEVICE_ID, "Laptop", "Dell", DeviceState.IN_USE, Instant.now());

        when(updateDeviceUseCase.patch(eq(DEVICE_ID), eq(patchFields))).thenReturn(patchedResponse);

        // Act & Assert
        mockMvc.perform(patch(BASE_URL + "/{id}", DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchFields)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("IN_USE"));

        verify(updateDeviceUseCase, times(1)).patch(eq(DEVICE_ID), eq(patchFields));
    }

    // --- DELETE /devices/{id} ---
    @Test
    void deleteDevice_shouldReturn204_whenSuccessful() throws Exception {
        // Arrange
        doNothing().when(deleteDeviceUseCase).deleteDevice(eq(DEVICE_ID));

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", DEVICE_ID))
                .andExpect(status().isNoContent());

        verify(deleteDeviceUseCase, times(1)).deleteDevice(eq(DEVICE_ID));
    }

    @Test
    void deleteDevice_shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        doThrow(new DeviceNotFoundException("Device not found"))
                .when(deleteDeviceUseCase).deleteDevice(eq(NON_EXISTENT_ID));

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound()); // Again, assuming exception mapping

        verify(deleteDeviceUseCase, times(1)).deleteDevice(eq(NON_EXISTENT_ID));
    }
    @Test
    void deleteDevice_shouldReturn404_whenInUse() throws Exception {
        // Arrange
        doThrow(new InvalidOperationException("Device with id" + DEVICE_ID + " not found"))
                .when(deleteDeviceUseCase).deleteDevice(eq(DEVICE_ID));

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", DEVICE_ID))
                .andExpect(status().isBadRequest()); // Again, assuming exception mapping

        verify(deleteDeviceUseCase, times(1)).deleteDevice(eq(DEVICE_ID));
    }
}