package com.global.devices.devicesapi.domain.model;

import com.global.devices.devicesapi.adaptor.outadaptor.repository.model.DeviceEntity;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;

import java.time.Instant;
import java.util.UUID;

public class Device {
    private final UUID id;
    private String name;
    private String brand;
    private DeviceState state;
    private final Instant creationTime;

    public Device(String name, String brand, DeviceState state) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = Instant.now();
    }

    public Device(UUID id, String name, String brand, DeviceState state, Instant creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = creationTime;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public DeviceState getState() { return state; }
    public Instant getCreationTime() { return creationTime; }


    public DeviceUseCaseResponse toUseCaseResponse() {
        return DeviceUseCaseResponse.builder()
                .id(this.id)
                .name(this.name)
                .brand(this.brand)
                .state(this.state)
                .creationTime(this.creationTime)
                .build();
    }

    public DeviceEntity toEntity() {
        return DeviceEntity.builder()
                .id(this.id)
                .name(this.name)
                .brand(this.brand)
                .state(this.state)
                .creationTime(this.creationTime)
                .build();
    }
}
