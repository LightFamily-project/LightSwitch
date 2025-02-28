package org.lightswitch.sdk.exception;

public class LightSwitchCastException extends RuntimeException {

    public LightSwitchCastException(String key, Object value, Class<?> expectedType) {
        super(String.format("Feature flag '%s' expected type %s but got value: %s (type: %s)",
                key, expectedType.getSimpleName(), value, value.getClass().getSimpleName()));
    }
}