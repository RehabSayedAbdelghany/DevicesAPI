package com.global.devices.devicesapi.adaptor.outadaptor.repository;

import com.global.devices.devicesapi.domain.model.DeviceState;
import com.global.devices.devicesapi.adaptor.outadaptor.repository.model.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceJpaRepository extends JpaRepository<DeviceEntity, UUID> {
    List<DeviceEntity> findByBrand(String brand);
    List<DeviceEntity> findByState(DeviceState state);
}