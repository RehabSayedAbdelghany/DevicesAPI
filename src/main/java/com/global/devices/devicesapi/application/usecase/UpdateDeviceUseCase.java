package com.global.devices.devicesapi.application.usecase;


import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;

import java.util.Map;
import java.util.UUID;

public interface UpdateDeviceUseCase {
    DeviceUseCaseResponse update(UUID deviceId, DeviceUseCaseRequest request);
    DeviceUseCaseResponse patch(UUID deviceId, Map<String, Object> updatedFields);
}
