package com.global.devices.devicesapi.adaptor.inadaptor.rest.dto;

import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.domain.model.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
Request to create a new device
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "state is required")
    private DeviceState state;

    public DeviceUseCaseRequest toUseCaseRequest() {
        return DeviceUseCaseRequest.builder()
                .name(this.name)
                .brand(this.brand)
                .state(this.state)
                .build();
    }
}
