package org.lightswitch.sdk.config;

import org.junit.jupiter.api.Test;
import org.lightswitch.sdk.exception.LightSwitchConfigException;

import static org.junit.jupiter.api.Assertions.*;

class LightSwitchConfigTest {

    @Test
    void testValidConfig() throws LightSwitchConfigException {
        LightSwitchConfig config = new LightSwitchConfig.Builder()
                .serverUrl("https://api.lightswitch.com")
                .sdkKey("test-key")
                .useCache(false)
                .reconnectDelay(3000)
                .connectionTimeout(15000)
                .build();

        assertEquals("https://api.lightswitch.com", config.getServerUrl());
        assertEquals("test-key", config.getSdkKey());
        assertFalse(config.isUseCache());
        assertEquals(3000, config.getReconnectDelay());
        assertEquals(15000, config.getConnectionTimeout());
    }

    @Test
    void testDefaultValues() throws LightSwitchConfigException {
        LightSwitchConfig config = new LightSwitchConfig.Builder()
                .serverUrl("https://api.lightswitch.com")
                .sdkKey("test-key")
                .build();

        assertTrue(config.isUseCache());
        assertEquals(5000, config.getReconnectDelay());
        assertEquals(10000, config.getConnectionTimeout());
    }

    @Test
    void testNegativeTimeoutUsesDefault() throws LightSwitchConfigException {
        LightSwitchConfig config = new LightSwitchConfig.Builder()
                .serverUrl("https://api.lightswitch.com")
                .sdkKey("test-key")
                .connectionTimeout(-5)
                .build();

        assertEquals(10000, config.getConnectionTimeout());
    }

    @Test
    void testNullServerUrlThrowsException() {
        Exception exception = assertThrows(LightSwitchConfigException.class, () ->
                new LightSwitchConfig.Builder().serverUrl(null).sdkKey("test-key").build()
        );
        assertEquals("Server URL cannot be null", exception.getMessage());
    }
}