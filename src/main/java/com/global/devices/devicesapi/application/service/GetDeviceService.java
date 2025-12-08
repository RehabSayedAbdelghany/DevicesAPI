package com.global.devices.devicesapi.application.service;

import com.global.devices.devicesapi.application.datasource.DeviceRepositoryWrapper;
import com.global.devices.devicesapi.application.usecase.GetDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.exception.DeviceNotFoundException;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetDeviceService implements GetDeviceUseCase {

    private final DeviceRepositoryWrapper repository;

    @Override
    public List<DeviceUseCaseResponse> getAll() {

         return toUseCaseResponseList(repository.getAllDevices());
    }

    @Override
    public DeviceUseCaseResponse findById(UUID id) {

        Optional<Device> device = repository.findById(id);
        if(device.isEmpty()){
            throw new DeviceNotFoundException(("Device with id " + id + " not found"));
        }
        return device.get().toUseCaseResponse();
    }

    @Override
    public List<DeviceUseCaseResponse> getByBrand(String brand) {

        return toUseCaseResponseList(repository.findByBrand(brand));
    }

    @Override
    public List<DeviceUseCaseResponse> getByState(DeviceState deviceState) {

        return toUseCaseResponseList(repository.findByState(deviceState));
    }

    public static List<DeviceUseCaseResponse> toUseCaseResponseList(List<Device> devices) {
        if (devices == null || devices.isEmpty()) {
            return List.of();
        }

        return devices.stream()
                // Use the method already defined inside the Device class
                .map(Device::toUseCaseResponse)
                .collect(Collectors.toList());
    }


}
