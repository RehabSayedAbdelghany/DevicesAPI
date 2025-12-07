package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.PaymentRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.CreateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.dto.DeviceRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceResponse;
import com.global.devices.devicesapi.domain.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDeviceService implements CreateDeviceUseCase {

    private final PaymentRepositoryWrapper repository;

    @Override
    public DeviceResponse create(DeviceRequest request) {
        Device device = new Device(request.getName(), request.getBrand(), request.getState());
         return repository.saveDevice(device).toUseCaseResponse();
    }
}
