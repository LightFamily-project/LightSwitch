package org.lightswitch.sdk.model;

import org.lightswitch.sdk.user.LightSwitchUser;

import java.util.Collections;
import java.util.Map;

public class FeatureFlag {

    private final String key;
    private final Object defaultValue;
    private final Map<Map<String, String>, Object> filters; // filtering rules

    public FeatureFlag(String key, Object defaultValue, Map<Map<String, String>, Object> filters) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.filters = filters != null ? filters : Collections.emptyMap();
    }

    public String getKey() {
        return key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Map<Map<String, String>, Object> getConditions() {
        return Collections.unmodifiableMap(filters);
    }

    /**
     * Find and return appropriate flag values based on user properties
     */
    public Object evaluate(LightSwitchUser user) {
        for (Map.Entry<Map<String, String>, Object> entry : filters.entrySet()) {
            Map<String, String> conditions = entry.getKey();
            boolean match = conditions.entrySet().stream()
                    .allMatch(cond -> user.getAttributes().getOrDefault(cond.getKey(), "").equals(cond.getValue()));

            if (match) {
                return entry.getValue();
            }
        }
        return defaultValue;
    }

    @Override
    public String toString() {
        return "FeatureFlag{" +
                "key='" + key + '\'' +
                ", defaultValue=" + defaultValue +
                ", filters=" + filters +
                '}';
    }
}
