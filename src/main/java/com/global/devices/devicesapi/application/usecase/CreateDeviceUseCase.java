package com.global.devices.devicesapi.application.usecase;


import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;

public interface CreateDeviceUseCase{
    DeviceUseCaseResponse create(DeviceUseCaseRequest request);
}
