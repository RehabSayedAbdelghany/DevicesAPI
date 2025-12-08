package com.global.devices.devicesapi.application.usecase.dto;

import com.global.devices.devicesapi.domain.model.DeviceState;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DeviceUseCaseResponse {
    private UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private Instant creationTime;
}