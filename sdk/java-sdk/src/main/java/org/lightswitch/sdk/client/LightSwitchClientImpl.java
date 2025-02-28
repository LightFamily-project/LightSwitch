package org.lightswitch.sdk.client;

import org.lightswitch.sdk.exception.LightSwitchCastException;
import org.lightswitch.sdk.model.FeatureFlag;
import org.lightswitch.sdk.user.LightSwitchUser;

import java.util.HashMap;
import java.util.Map;

public class LightSwitchClientImpl implements LightSwitchClient {

    // TODO develop FeatureFlagRepository, httpClient, SSEClient
    private final Map<String, FeatureFlag> featureFlags; // Mock

    public LightSwitchClientImpl() {
        // Mock data
        this.featureFlags = new HashMap<>();

        featureFlags.put("new-dashboard", new FeatureFlag(
                "new-dashboard",
                false, // default
                Map.of(
                        Map.of("region", "US"), true // active "US" user
                )
        ));

        featureFlags.put("beta-feature", new FeatureFlag(
                "beta-feature",
                false,
                Map.of(
                        Map.of("plan", "premium"), true // active "premium" user
                )
        ));

        featureFlags.put("discount", new FeatureFlag(
                "discount",
                0, // default sale rates : 0%
                Map.of(
                        Map.of("region", "US"), 100,  // US user 100% sale
                        Map.of("region", "EU"), 50   // EU user 50% sale
                )
        ));
    }

    private FeatureFlag getMockFlag(String key) {
        return featureFlags.get(key);
    }

    private <T> T castValue(String key, Object value, Class<T> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new LightSwitchCastException(key, value, expectedType);
        }
        return expectedType.cast(value);
    }

    @Override
    public boolean getBooleanFlag(String key, boolean defaultValue, LightSwitchUser user) {
        FeatureFlag flag = getMockFlag(key);
        if (flag == null) return defaultValue;

        Object value = flag.evaluate(user);
        return castValue(key, value, Boolean.class);
    }

    @Override
    public int getIntFlag(String key, int defaultValue, LightSwitchUser user) {
        FeatureFlag flag = getMockFlag(key);
        if (flag == null) return defaultValue;

        Object value = flag.evaluate(user);
        return castValue(key, value, Integer.class);
    }

    @Override
    public String getStringFlag(String key, String defaultValue, LightSwitchUser user) {
        FeatureFlag flag = getMockFlag(key);
        if (flag == null) return defaultValue;

        Object value = flag.evaluate(user);
        return castValue(key, value, String.class);
    }

    @Override
    public void destroy() {
        featureFlags.clear();
    }

    @Override
    public boolean isEnabled(String key, LightSwitchUser user) {
        return getBooleanFlag(key, false, user);
    }
}
