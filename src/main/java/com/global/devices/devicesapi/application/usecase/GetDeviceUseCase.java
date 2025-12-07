package com.global.devices.devicesapi.application.usecase;


import com.global.devices.devicesapi.application.usecase.dto.DeviceResponse;
import com.global.devices.devicesapi.application.usecase.dto.GetDeviceResponse;
import com.global.devices.devicesapi.domain.model.DeviceState;

import java.util.List;
import java.util.UUID;

public interface GetDeviceUseCase {
    List<DeviceResponse> getAll();
    DeviceResponse findById(UUID deviceId);
    List<DeviceResponse> getByBrand(String brand);
    List<DeviceResponse> getByState(DeviceState deviceState);
}
