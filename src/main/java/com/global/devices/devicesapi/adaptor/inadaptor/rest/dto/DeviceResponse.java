package com.global.devices.devicesapi.adaptor.inadaptor.rest.dto;

import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.model.DeviceState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
    private UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private Instant creationTime;

    public static DeviceResponse from(DeviceUseCaseResponse device) {
        return com.global.devices.devicesapi.adaptor.inadaptor.rest.dto.DeviceResponse.builder()
                .id(device.getId())
                .name(device.getName())
                .brand(device.getBrand())
                .state(device.getState())
                .creationTime(device.getCreationTime())
                .build();
    }
}