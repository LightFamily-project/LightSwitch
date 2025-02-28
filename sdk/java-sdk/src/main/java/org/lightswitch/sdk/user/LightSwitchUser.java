package org.lightswitch.sdk.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LightSwitchUser {

    private final String userId;
    private final Map<String, String> attributes;

    public LightSwitchUser(String userId) {
        this.userId = userId;
        this.attributes = new HashMap<>();
    }

    public LightSwitchUser(String userId, Map<String, String> attributes) {
        this.userId = userId;
        this.attributes = new HashMap<>(attributes);
    }

    public String getUserId() {
        return userId;
    }

    public LightSwitchUser addAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
