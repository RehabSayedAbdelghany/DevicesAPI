package com.global.devices.devicesapi.adaptor.inadaptor.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.devices.devicesapi.adaptor.inadaptor.rest.dto.DeviceRequest;
import com.global.devices.devicesapi.adaptor.inadaptor.rest.dto.DeviceResponse;
import com.global.devices.devicesapi.application.usecase.CreateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.DeleteDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.GetDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.UpdateDeviceUseCase;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseRequest;
import com.global.devices.devicesapi.application.usecase.dto.DeviceUseCaseResponse;
import com.global.devices.devicesapi.domain.exception.DeviceNotFoundException;
import com.global.devices.devicesapi.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DeviceControllerTest {



    @Mock
    private CreateDeviceUseCase createDeviceUseCase;
    @Mock
    private GetDeviceUseCase getDeviceUseCase;
    @Mock
    private UpdateDeviceUseCase updateDeviceUseCase;
    @Mock
    private DeleteDeviceUseCase deleteDeviceUseCase;

    @InjectMocks
    private DeviceController deviceController;

    private DeviceRequest deviceRequest;
    private DeviceResponse deviceResponse;
    private DeviceUseCaseRequest deviceUseCaseRequest;
    private DeviceUseCaseResponse deviceUseCaseResponse;
    private UUID deviceId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        deviceId = UUID.randomUUID();

        deviceRequest = new DeviceRequest("name", "brand", DeviceState.IN_USE);
        deviceUseCaseRequest = deviceRequest.toUseCaseRequest();
        deviceResponse = new DeviceResponse(deviceId, "name", "brand", DeviceState.AVAILABLE, Instant.now());
        deviceUseCaseResponse = new DeviceUseCaseResponse(deviceResponse.getId(),deviceResponse.getName(),deviceResponse.getBrand(),deviceResponse.getState(),deviceResponse.getCreationTime());
    }

    @Test
    void createDevice_Should_call_createUseCase()  {


        when(createDeviceUseCase.create(deviceRequest.toUseCaseRequest())).thenReturn(deviceUseCaseResponse);

        ResponseEntity<DeviceResponse> response = deviceController.createDevice(deviceRequest);

        assertNotNull(response);
        assertEquals(deviceUseCaseResponse.getId(), response.getBody().getId());
        verify(createDeviceUseCase).create(deviceUseCaseRequest);
        verifyNoInteractions(updateDeviceUseCase);
        verifyNoInteractions(deleteDeviceUseCase);
        verifyNoInteractions(getDeviceUseCase);

    }

    @Test
    void deleteDevice_Should_call_DeleteUseCase()  {


        doNothing().when(deleteDeviceUseCase).deleteDevice(deviceId);

        ResponseEntity<Void> response = deviceController.deleteDevice(deviceId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteDeviceUseCase).deleteDevice(deviceId);
        verifyNoInteractions(updateDeviceUseCase);
        verifyNoInteractions(createDeviceUseCase);
        verifyNoInteractions(getDeviceUseCase);

    }

    @Test
    void getAllDevices_Should_call_getUseCase()  {

        List<DeviceUseCaseResponse> list = Collections.singletonList(deviceUseCaseResponse);
        when(getDeviceUseCase.getAll()).thenReturn(list);

        List<DeviceUseCaseResponse> responseList = getDeviceUseCase.getAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(getDeviceUseCase).getAll();
        verifyNoInteractions(updateDeviceUseCase);
        verifyNoInteractions(createDeviceUseCase);
        verifyNoInteractions(deleteDeviceUseCase);
    }


    @Test
    void getDevice_byId_Should_call_getUseCase()  {


        when(getDeviceUseCase.findById(deviceId)).thenReturn(deviceUseCaseResponse);

        ResponseEntity<DeviceResponse> response = deviceController.getDeviceById(deviceId);

        assertNotNull(response);
        assertEquals(deviceUseCaseResponse.getId(), response.getBody().getId());
        verify(getDeviceUseCase).findById(deviceId);
        verifyNoInteractions(updateDeviceUseCase);
        verifyNoInteractions(deleteDeviceUseCase);
        verifyNoInteractions(createDeviceUseCase);

    }

    @Test
    void getDevices_byState_Should_call_getUseCase()  {

        List<DeviceUseCaseResponse> list = Collections.singletonList(deviceUseCaseResponse);
        when(getDeviceUseCase.getByState( DeviceState.IN_USE)).thenReturn(list);

        List<DeviceUseCaseResponse> responseList = getDeviceUseCase.getByState(DeviceState.IN_USE);

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(getDeviceUseCase).getByState(DeviceState.IN_USE);
        verifyNoInteractions(updateDeviceUseCase);
        verifyNoInteractions(createDeviceUseCase);
        verifyNoInteractions(deleteDeviceUseCase);
    }

    @Test
    void getDevices_byBrand_Should_call_getUseCase()  {

        List<DeviceUseCaseResponse> list = Collections.singletonList(deviceUseCaseResponse);
        when(getDeviceUseCase.getByBrand("brand")).thenReturn(list);

        List<DeviceUseCaseResponse> responseList = getDeviceUseCase.getByBrand("brand");

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(getDeviceUseCase).getByBrand("brand");
        verifyNoInteractions(updateDeviceUseCase);
        verifyNoInteractions(createDeviceUseCase);
        verifyNoInteractions(deleteDeviceUseCase);
    }

    @Test
    void updateDevice__Should_call_updateUseCase()  {
        when(updateDeviceUseCase.update(deviceId,deviceRequest.toUseCaseRequest())).thenReturn(deviceUseCaseResponse);

        ResponseEntity<DeviceResponse> response = deviceController.updateDevice(deviceId,deviceRequest);

        assertNotNull(response);
        assertEquals(deviceUseCaseResponse.getId(), response.getBody().getId());
        verify(updateDeviceUseCase).update(deviceId,deviceUseCaseRequest);
        verifyNoInteractions(createDeviceUseCase);
        verifyNoInteractions(deleteDeviceUseCase);
        verifyNoInteractions(getDeviceUseCase);
    }

    @Test
    void updateDevicePatch_Should_call_updateUseCase()  {
        deviceUseCaseResponse = new DeviceUseCaseResponse(deviceResponse.getId(),deviceResponse.getName(),"updateBrand",deviceResponse.getState(),deviceResponse.getCreationTime());

        Map<String, Object> updates = new HashMap<>();
        updates.put("brand", "updateBrand");

        when(updateDeviceUseCase.patch(deviceId, updates)).thenReturn(deviceUseCaseResponse);

        ResponseEntity<DeviceResponse>  response = deviceController.updateDevice(deviceId, updates);

        assertNotNull(response);
        assertEquals(deviceUseCaseResponse.getId(), response.getBody().getId());
        assertEquals(deviceUseCaseResponse.getBrand(), response.getBody().getBrand());
        verify(updateDeviceUseCase).patch(deviceId,updates);
        verifyNoInteractions(createDeviceUseCase);
        verifyNoInteractions(deleteDeviceUseCase);
        verifyNoInteractions(getDeviceUseCase);
    }

}
