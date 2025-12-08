package com.global.devices.devicesapi.adaptor.inadaptor.rest;

import com.global.devices.devicesapi.adaptor.inadaptor.rest.dto.DeviceRequest;
import com.global.devices.devicesapi.adaptor.inadaptor.rest.dto.DeviceResponse;
import com.global.devices.devicesapi.application.usecase.CreateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.DeleteDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.GetDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.UpdateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.exception.ErrorResponse;
import com.global.devices.devicesapi.domain.model.DeviceState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/devices-api/v1/devices")
public class DeviceController {

    private final CreateDeviceUseCase createDeviceUseCase;
    private final GetDeviceUseCase getDeviceUseCase;
    private final DeleteDeviceUseCase deleteDeviceUseCase;
    private final UpdateDeviceUseCase updateDeviceUseCase;

    @PostMapping
    @Operation(summary = "Create a new device")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Device created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody DeviceRequest request) {
        var useCaseRequest = request.toUseCaseRequest();
        var serviceResponse = createDeviceUseCase.create(useCaseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceResponse.from(serviceResponse));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update of device")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "400", description = "Invalid update")
    })
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable UUID id,
            @RequestBody DeviceRequest request) {
        var useCaseRequest = request.toUseCaseRequest();
        var response = updateDeviceUseCase.update(id, useCaseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceResponse.from(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a device by ID", description = "Retrieves a single device by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found",
                    content = @Content(schema = @Schema(implementation = DeviceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DeviceResponse> getDeviceById(
            @Parameter(description = "Device ID") @PathVariable UUID id) {
        return ResponseEntity.ok(DeviceResponse.from(getDeviceUseCase.findById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all devices", description = "Retrieves all devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully")
    })
    public ResponseEntity<List<DeviceResponse>> getDevices() {

        return ResponseEntity.ok(fromList(getDeviceUseCase.getAll()));
    }
    private  List<DeviceResponse> fromList(List<DeviceUseCaseResponse> serviceResponses) {
        if (serviceResponses == null || serviceResponses.isEmpty()) {
            return List.of(); // Return an empty, immutable list
        }

        return serviceResponses.stream()
                // Apply the single-object mapping method to every element in the list
                .map(DeviceResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping(params = "brand")
    @Operation(summary = "Get devices by brand", description = "Retrieve devices by brand")
    public ResponseEntity<List<DeviceResponse>>getByBrand(@RequestParam String brand) {
        return ResponseEntity.ok(fromList(getDeviceUseCase.getByBrand(brand)));
    }

    @GetMapping(params = "state")
    @Operation(summary = "Get devices by state", description = "Retrieve all devices by state")
    public ResponseEntity<List<DeviceResponse>> getByState(@RequestParam DeviceState state) {
        return ResponseEntity.ok(fromList(getDeviceUseCase.getByState(state)));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device", description = "Deletes a device by its ID (cannot delete IN_USE devices)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete IN_USE device",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "Device ID") @PathVariable UUID id) {
        deleteDeviceUseCase.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update of device")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "400", description = "Invalid update")
    })
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updatedFields) {

        var response = updateDeviceUseCase.patch(id, updatedFields);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceResponse.from(response));
    }
}
