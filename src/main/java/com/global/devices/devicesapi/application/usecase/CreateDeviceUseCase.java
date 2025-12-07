package com.global.devices.devicesapi.application.usecase;


import com.global.devices.devicesapi.application.usecase.dto.DeviceRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceResponse;

public interface CreateDeviceUseCase{
    DeviceResponse create(DeviceRequest request);
}
