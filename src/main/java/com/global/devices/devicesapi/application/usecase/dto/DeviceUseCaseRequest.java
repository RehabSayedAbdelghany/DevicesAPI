package com.global.devices.devicesapi.application.usecase.dto;

import com.global.devices.devicesapi.domain.model.DeviceState;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DeviceUseCaseRequest {
    private String name;
    private String brand;
    private DeviceState state;
}