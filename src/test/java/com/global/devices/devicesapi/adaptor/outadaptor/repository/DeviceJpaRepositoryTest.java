package com.global.devices.devicesapi.adaptor.outadaptor.repository;

import com.global.devices.devicesapi.domain.model.DeviceState;
import com.global.devices.devicesapi.adaptor.outadaptor.repository.model.DeviceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class DeviceJpaRepositoryTest {

    @Autowired
    private DeviceJpaRepository deviceJpaRepository;

    private DeviceEntity activeAcmeDevice;
    private DeviceEntity inactiveAcmeDevice;
    private DeviceEntity activeBetaDevice;

    @BeforeEach
    void setUp() {
        // Clear the database and set up consistent test data
        deviceJpaRepository.deleteAll();

        activeAcmeDevice = DeviceEntity.builder()
                .id(UUID.randomUUID())
                .name("Laptop X1")
                .brand("AcmeTech")
                .state(DeviceState.AVAILABLE)
                .creationTime(Instant.now())
                .build();

        inactiveAcmeDevice = DeviceEntity.builder()
                .id(UUID.randomUUID())
                .name("Monitor Z")
                .brand("AcmeTech")
                .state(DeviceState.INACTIVE)
                .creationTime(Instant.now())
                .build();

        activeBetaDevice = DeviceEntity.builder()
                .id(UUID.randomUUID())
                .name("Smartphone Y")
                .brand("BetaCorp")
                .state(DeviceState.AVAILABLE)
                .creationTime(Instant.now())
                .build();

        // Save entities to the test database
        deviceJpaRepository.saveAll(List.of(activeAcmeDevice, inactiveAcmeDevice, activeBetaDevice));
    }

    // --- Testing Custom Query Methods ---

    @Test
    void findByBrand_ShouldReturnCorrectDevices() {
        // Act
        List<DeviceEntity> acmeDevices = deviceJpaRepository.findByBrand("AcmeTech");

        // Assert
        assertThat(acmeDevices).hasSize(2);
        assertThat(acmeDevices)
                .extracting(DeviceEntity::getName)
                .containsExactlyInAnyOrder("Laptop X1", "Monitor Z");
    }

    @Test
    void findByState_ShouldReturnOnlyActiveDevices() {
        // Act
        List<DeviceEntity> activeDevices = deviceJpaRepository.findByState(DeviceState.AVAILABLE);

        // Assert
        assertThat(activeDevices).hasSize(2);
        assertThat(activeDevices)
                .extracting(DeviceEntity::getBrand)
                .containsExactlyInAnyOrder("AcmeTech", "BetaCorp");
    }

    @Test
    void findByState_ShouldReturnEmptyListForNonExistentState() {
        // Act
        List<DeviceEntity> devices = deviceJpaRepository.findByState(DeviceState.IN_USE);

        // Assert
        assertThat(devices).isEmpty();
    }

    // --- Testing Inherited CRUD Methods ---

    @Test
    void save_ShouldPersistNewDevice() {
        // Arrange
        DeviceEntity newDevice = DeviceEntity.builder()
                .id(UUID.randomUUID())
                .name("Drone")
                .brand("Fly")
                .state(DeviceState.AVAILABLE)
                .creationTime(Instant.now())
                .build();

        // Act
        DeviceEntity savedDevice = deviceJpaRepository.save(newDevice);

        // Assert
        assertThat(savedDevice).isNotNull();
        assertThat(deviceJpaRepository.findById(newDevice.getId())).isPresent();
    }
}