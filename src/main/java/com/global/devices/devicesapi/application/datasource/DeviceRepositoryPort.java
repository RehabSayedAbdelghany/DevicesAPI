package com.global.devices.devicesapi.application.datasource;

import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public interface DeviceRepositoryPort {
    Device save(Device device);
    Optional<Device> findById(UUID id);
    List<Device> findAll();
    List<Device> findByBrand(String brand);
    List<Device> findByState(DeviceState state);
    void deleteById(UUID id);
}