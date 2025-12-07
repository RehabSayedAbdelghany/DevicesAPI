package com.global.devices.devicesapi.application.usecase;


import com.global.devices.devicesapi.application.usecase.dto.DeviceResponse;
import com.global.devices.devicesapi.domain.model.DeviceState;

import java.util.List;
import java.util.UUID;

public interface DeleteDeviceUseCase {
    void deleteDevice(UUID deviceId);

}
