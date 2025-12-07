package com.global.devices.devicesapi.domain.exception;


public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}