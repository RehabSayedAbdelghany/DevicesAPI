package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.PaymentRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.DeleteDeviceUseCase;
import com.global.devices.devicesapi.domain.exception.DeviceNotFoundException;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.validation.DeviceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDeviceService implements DeleteDeviceUseCase {

    private final PaymentRepositoryWrapper repository;

    private  final DeviceValidator deviceValidator;

    @Override
    public void deleteDevice(UUID deviceId) {
        Device Device   =  repository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException("Device with id " + deviceId + " not found"));
        deviceValidator.validateStatus(Device);
        repository.deleteDeviceById(deviceId);
    }
}
