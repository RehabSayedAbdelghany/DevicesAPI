package com.global.devices.devicesapi.application.datasource;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceRepositoryWrapperTest { // Corrected class name based on the service file

    // Mock the dependency (the Port interface)
    @Mock
    private DeviceRepositoryPort deviceRepositoryPort;

    // Inject the mocks into the class being tested
    @InjectMocks
    private DeviceRepositoryWrapper deviceRepositoryWrapper;

    private Device mockDevice;
    private final UUID TEST_ID = UUID.randomUUID();
    private final Instant TEST_TIME = Instant.now(); // Needed for the all-args constructor

    @BeforeEach
    void setUp() {
        // FIX: Replaced Device.builder() with the all-arguments constructor
        // to match the structure of your domain model:
        // public Device(UUID id, String name, String brand, DeviceState state, Instant creationTime)
        mockDevice = new Device(
                TEST_ID,
                "Test Device",
                "Acme",
                DeviceState.AVAILABLE,
                TEST_TIME
        );
    }

    // --- 1. Test saveDevice ---

    @Test
    void saveDevice_ShouldDelegateToPortSaveAndReturnResult() {
        // Arrange
        // When port.save is called with any Device, return the mockDevice
        when(deviceRepositoryPort.save(any(Device.class))).thenReturn(mockDevice);

        // Act
        Device result = deviceRepositoryWrapper.saveDevice(mockDevice);

        // Assert
        // Verify that the port's save method was called exactly once with the correct object
        verify(deviceRepositoryPort, times(1)).save(mockDevice);
        assertEquals(TEST_ID, result.getId());
    }

    // --- 2. Test getAllDevices ---

    @Test
    void getAllDevices_ShouldDelegateToPortFindAll() {
        // Arrange
        List<Device> expectedList = List.of(mockDevice);
        when(deviceRepositoryPort.findAll()).thenReturn(expectedList);

        // Act
        List<Device> result = deviceRepositoryWrapper.getAllDevices();

        // Assert
        // Verify that the port's findAll method was called
        verify(deviceRepositoryPort, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(expectedList, result);
    }

    // --- 3. Test findById ---

    @Test
    void findById_ShouldDelegateToPortFindById() {
        // Arrange
        Optional<Device> expectedOptional = Optional.of(mockDevice);
        when(deviceRepositoryPort.findById(TEST_ID)).thenReturn(expectedOptional);

        // Act
        Optional<Device> result = deviceRepositoryWrapper.findById(TEST_ID);

        // Assert
        verify(deviceRepositoryPort, times(1)).findById(TEST_ID);
        assertTrue(result.isPresent());
        assertEquals(TEST_ID, result.get().getId());
    }

    // --- 4. Test findByBrand ---

    @Test
    void findByBrand_ShouldDelegateToPortFindByBrand() {
        // Arrange
        String brand = "Acme";
        List<Device> expectedList = List.of(mockDevice);
        when(deviceRepositoryPort.findByBrand(brand)).thenReturn(expectedList);

        // Act
        List<Device> result = deviceRepositoryWrapper.findByBrand(brand);

        // Assert
        verify(deviceRepositoryPort, times(1)).findByBrand(brand);
        assertEquals(expectedList, result);
    }

    // --- 5. Test findByState ---

    @Test
    void findByState_ShouldDelegateToPortFindByState() {
        // Arrange
        DeviceState state = DeviceState.AVAILABLE;
        List<Device> expectedList = List.of(mockDevice);
        when(deviceRepositoryPort.findByState(state)).thenReturn(expectedList);

        // Act
        List<Device> result = deviceRepositoryWrapper.findByState(state);

        // Assert
        verify(deviceRepositoryPort, times(1)).findByState(state);
        assertEquals(expectedList, result);
    }

    // --- 6. Test deleteDeviceById ---

    @Test
    void deleteDeviceById_ShouldDelegateToDeleteById() {
        // Act
        deviceRepositoryWrapper.deleteDeviceById(TEST_ID);

        // Assert
        // Verify that the port's deleteById method was called exactly once
        verify(deviceRepositoryPort, times(1)).deleteById(TEST_ID);
    }
}