package com.global.devices.devicesapi.application.datasource;

import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceRepositoryWrapper {
    private final DeviceRepositoryPort deviceRepositoryPort;

    public Device saveDevice(Device device){
        return deviceRepositoryPort.save(device); // here we can have domain events and publish it
    }

    public List<Device> getAllDevices(){
        return deviceRepositoryPort.findAll();
    }

    public Optional<Device> findById(UUID deviceId){
        return deviceRepositoryPort.findById(deviceId);
    }

    public List<Device> findByBrand(String brand){
        return deviceRepositoryPort.findByBrand(brand);
    }

    public List<Device> findByState(DeviceState state){
        return deviceRepositoryPort.findByState(state);
    }

    public void deleteDeviceById(UUID deviceId){
        deviceRepositoryPort.deleteById(deviceId);
    }
}
