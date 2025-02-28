package org.lightswitch.sdk.client;

import org.lightswitch.sdk.user.LightSwitchUser;

public interface LightSwitchClient {

    boolean getBooleanFlag(String key, boolean defaultValue, LightSwitchUser user);

    int getIntFlag(String key, int defaultValue, LightSwitchUser user);

    String getStringFlag(String key, String defaultValue, LightSwitchUser user);

    void destroy();

    boolean isEnabled(String key, LightSwitchUser user);
}
