package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.DeviceRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.CreateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDeviceService implements CreateDeviceUseCase {

    private final DeviceRepositoryWrapper repository;

    @Override
    public DeviceUseCaseResponse create(DeviceUseCaseRequest request) {
        Device device = new Device(request.getName(), request.getBrand(), request.getState());
         return repository.saveDevice(device).toUseCaseResponse();
    }
}
