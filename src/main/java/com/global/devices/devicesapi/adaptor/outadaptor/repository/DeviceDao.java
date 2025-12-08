package com.global.devices.devicesapi.adaptor.outadaptor.repository;

import com.global.devices.devicesapi.adaptor.outadaptor.repository.model.DeviceEntity;
import com.global.devices.devicesapi.application.datasource.DeviceRepositoryPort;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeviceDao implements DeviceRepositoryPort {

    private final DeviceJpaRepository jpaRepository;


    @Override
    public Device save(Device device) {
        DeviceEntity entity = device.toEntity();
        DeviceEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }



    private  Device toDomain(DeviceEntity entity) {
        return new Device(
                entity.getId(),
                entity.getName(),
                entity.getBrand(),
                entity.getState(),
                entity.getCreationTime()
        );
    }


    @Override
    public List<Device> findAll() {
        List<DeviceEntity> entities = jpaRepository.findAll();
        return toDomainList(entities);
    }

    public List<Device> toDomainList(List<DeviceEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Device> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Device> findByBrand(String brand) {
        List<DeviceEntity> entities = jpaRepository.findByBrand(brand);
        return toDomainList(entities);
    }

    @Override
    public List<Device> findByState(DeviceState state) {
        List<DeviceEntity> entities = jpaRepository.findByState(state);
        return toDomainList(entities);
    }

    @Override
    public void deleteById(UUID deviceId) {
        jpaRepository.deleteById(deviceId);
    }
}