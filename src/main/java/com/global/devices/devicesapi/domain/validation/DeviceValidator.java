package com.global.devices.devicesapi.domain.validation;

import com.global.devices.devicesapi.domain.exception.InvalidOperationException;
import com.global.devices.devicesapi.domain.model.Device;
import com.global.devices.devicesapi.domain.model.DeviceState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeviceValidator {

    public void validateStatus(Device device) {
        log.debug("Validating delete for device {} with state {}",
                device.getId(), device.getState());

        if (device.getState() == DeviceState.IN_USE) {
            throw new InvalidOperationException("Device with id "+ device.getId()+" is currently in use and cannot be updated/deleted");
        }
    }
}
