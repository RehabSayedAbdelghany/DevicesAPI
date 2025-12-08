package com.global.devices.devicesapi.adaptor.outadaptor.repository;

import com.global.devices.devicesapi.adaptor.outadaptor.repository.model.DeviceEntity;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceDaoTest {

    @Mock
    private DeviceJpaRepository jpaRepository;

    @InjectMocks
    private DeviceDao deviceDao;

    private Device domainDevice;
    private DeviceEntity entityDevice;
    private final UUID DEVICE_ID = UUID.randomUUID();
    private final Instant NOW = Instant.now();

    @BeforeEach
    void setUp() {
        // Setup shared data for both domain and entity models
        domainDevice = new Device(
                DEVICE_ID,
                "TestDevice",
                "BrandX",
                DeviceState.AVAILABLE,
                NOW
        );

        entityDevice = DeviceEntity.builder()
                .id(DEVICE_ID)
                .name("TestDevice")
                .brand("BrandX")
                .state(DeviceState.AVAILABLE)
                .creationTime(NOW)
                .build();
    }

    // --- Testing Mapping and Delegation for SAVE ---

    @Test
    void save_ShouldConvertAndSaveEntity_AndReturnDomain() {
        when(jpaRepository.save(any(DeviceEntity.class))).thenReturn(entityDevice);


        Device savedDomain = deviceDao.save(domainDevice);


        verify(jpaRepository, times(1)).save(any(DeviceEntity.class));

        assertNotNull(savedDomain);
        assertEquals(DEVICE_ID, savedDomain.getId());
        assertEquals("TestDevice", savedDomain.getName());
    }

    // --- Testing findAll and List Mapping ---

    @Test
    void findAll_ShouldReturnAllMappedDevices() {

        List<DeviceEntity> mockEntities = List.of(entityDevice, entityDevice);
        when(jpaRepository.findAll()).thenReturn(mockEntities);


        List<Device> domainList = deviceDao.findAll();

        verify(jpaRepository, times(1)).findAll();
        assertNotNull(domainList);
        assertEquals(2, domainList.size());
        assertEquals(Device.class, domainList.get(0).getClass());
    }

    // --- Testing findById and Optional Mapping ---

    @Test
    void findById_ShouldReturnOptionalDomainWhenFound() {
        when(jpaRepository.findById(DEVICE_ID)).thenReturn(Optional.of(entityDevice));

        Optional<Device> result = deviceDao.findById(DEVICE_ID);

        assertTrue(result.isPresent());
        assertEquals(DEVICE_ID, result.get().getId());
        verify(jpaRepository, times(1)).findById(DEVICE_ID);
    }

    @Test
    void findById_ShouldReturnEmptyOptionalWhenNotFound() {
        when(jpaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<Device> result = deviceDao.findById(UUID.randomUUID());

        assertFalse(result.isPresent());
    }

    // --- Testing findByBrand and findByState ---

    @Test
    void findByBrand_ShouldReturnMappedList() {
        when(jpaRepository.findByBrand("BrandX")).thenReturn(List.of(entityDevice));

        List<Device> result = deviceDao.findByBrand("BrandX");

        assertEquals(1, result.size());
        assertEquals("BrandX", result.get(0).getBrand());
        verify(jpaRepository, times(1)).findByBrand("BrandX");
    }

    // --- Testing deleteById ---

    @Test
    void deleteById_ShouldDelegateToDelete() {
        deviceDao.deleteById(DEVICE_ID);

        verify(jpaRepository, times(1)).deleteById(DEVICE_ID);
    }
}