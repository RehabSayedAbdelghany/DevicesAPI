package com.global.devices.devicesapi.application.usecase;


import com.global.devices.devicesapi.application.usecase.dto.DeviceRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceResponse;

import java.util.Map;
import java.util.UUID;

public interface UpdateDeviceUseCase {
    DeviceResponse update(UUID deviceId, DeviceRequest request);
    DeviceResponse patch(UUID deviceId, Map<String, Object> updatedFields);
}
