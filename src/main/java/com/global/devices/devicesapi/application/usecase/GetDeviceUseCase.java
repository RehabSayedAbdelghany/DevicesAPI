package com.global.devices.devicesapi.application.usecase;


import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.model.DeviceState;

import java.util.List;
import java.util.UUID;

public interface GetDeviceUseCase {
    List<DeviceUseCaseResponse> getAll();
    DeviceUseCaseResponse findById(UUID deviceId);
    List<DeviceUseCaseResponse> getByBrand(String brand);
    List<DeviceUseCaseResponse> getByState(DeviceState deviceState);
}
