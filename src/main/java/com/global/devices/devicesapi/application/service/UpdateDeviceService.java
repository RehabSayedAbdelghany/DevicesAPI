package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.DeviceRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.UpdateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.exception.DeviceNotFoundException;
import com.global.devices.devicesapi.domain.exception.InvalidOperationException;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import com.global.devices.devicesapi.domain.validation.DeviceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateDeviceService implements UpdateDeviceUseCase {

    private final DeviceRepositoryWrapper repository;
    private  final DeviceValidator deviceValidator;
    @Override
    public DeviceUseCaseResponse update(UUID deviceId, DeviceUseCaseRequest request) {
        Device existingDevice = repository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(("Device with id " + deviceId + " not found")));

        deviceValidator.validateStatus(existingDevice);
        Device updatedDevice = new Device(
                deviceId,
                request.getName(),
                request.getBrand(),
                request.getState(),
                existingDevice.getCreationTime()
        );
        return repository.saveDevice(updatedDevice).toUseCaseResponse();

    }

    @Override
    public DeviceUseCaseResponse patch(UUID deviceId, Map<String, Object> updatedFields) {
        Device existingDevice = repository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(("Device with id " + deviceId + " not found")));

        deviceValidator.validateStatus(existingDevice);

        String name = existingDevice.getName();
        String brand = existingDevice.getBrand();
        DeviceState state = existingDevice.getState();
        Instant creationTime = existingDevice.getCreationTime();

        if (updatedFields.containsKey("name")) {
            name = (String) updatedFields.get("name");
        }
        if (updatedFields.containsKey("brand")) {
            brand = (String) updatedFields.get("brand");
        }
        if (updatedFields.containsKey("state")) {
            state = DeviceState.valueOf((String) updatedFields.get("state"));
        }
        if (updatedFields.containsKey("creationTime")) {
            throw new InvalidOperationException("Device with id "+ existingDevice.getId()+" creationTime can not be updated");

        }

        Device updatedDevice = new Device(existingDevice.getId(), name, brand, state, creationTime);
        return repository.saveDevice(updatedDevice).toUseCaseResponse();

    }
}


