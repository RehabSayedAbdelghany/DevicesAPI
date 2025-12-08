package com.global.devices.devicesapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private OpenApiConfig openApiConfig;

    @BeforeEach
    void setUp() {
        // Instantiate the configuration class directly
        openApiConfig = new OpenApiConfig();
    }

    @Test
    void devicesOpenAPI_ShouldConfigureMetadataCorrectly() {
        // Act
        OpenAPI openAPI = openApiConfig.devicesOpenAPI();

        // Assert
        assertNotNull(openAPI, "OpenAPI object should not be null.");

        // 1. Assert Info Object (Title, Version, Description)
        Info info = openAPI.getInfo();
        assertNotNull(info, "Info object must be present.");
        assertEquals("Devices Management API", info.getTitle(), "Title should be set correctly.");
        assertEquals("1.0.0", info.getVersion(), "Version should be set correctly.");
        assertTrue(info.getDescription().contains("CRUD operations"), "Description should be complete.");

        // 2. Assert Contact Information
        assertNotNull(info.getContact(), "Contact information should be present.");
        assertEquals("API Support", info.getContact().getName());
        assertEquals("support@company.com", info.getContact().getEmail());

        // 3. Assert License Information
        assertNotNull(info.getLicense(), "License information should be present.");
        assertEquals("MIT License", info.getLicense().getName());
        assertTrue(info.getLicense().getUrl().contains("MIT"), "License URL should be correct.");

        // 4. Assert Server URL
        assertNotNull(openAPI.getServers(), "Servers list should be present.");
        assertEquals(1, openAPI.getServers().size(), "Only one server should be configured.");

        Server localServer = openAPI.getServers().get(0);
        assertEquals("http://localhost:8080", localServer.getUrl(), "Local server URL should be correct.");
        assertEquals("Local development server", localServer.getDescription(), "Server description should be correct.");
    }
}