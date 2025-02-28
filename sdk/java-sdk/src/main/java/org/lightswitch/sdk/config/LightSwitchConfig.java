package org.lightswitch.sdk.config;

import org.lightswitch.sdk.exception.LightSwitchConfigException;

public class LightSwitchConfig {

    private final String serverUrl;
    private final String sdkKey;
    private final int reconnectDelay;
    private final int connectionTimeout;

    private LightSwitchConfig(Builder builder) {
        this.serverUrl = builder.serverUrl;
        this.sdkKey = builder.sdkKey;
        this.reconnectDelay = builder.reconnectDelay;
        this.connectionTimeout = builder.connectionTimeout;
    }

    public static class Builder {
        private String serverUrl;
        private String sdkKey;
        private int reconnectDelay = 5000;
        private int connectionTimeout = 10000;

        public Builder serverUrl(String serverUrl) throws LightSwitchConfigException {

            if (serverUrl == null) {
                throw new LightSwitchConfigException("Server URL cannot be null");
            }

            this.serverUrl = serverUrl;
            return this;
        }

        public Builder sdkKey(String sdkKey) {
            this.sdkKey = sdkKey;
            return this;
        }

        public Builder reconnectDelay(int reconnectDelay) {
            this.reconnectDelay = reconnectDelay;
            return this;
        }

        public Builder connectionTimeout(int connectionTimeout) {
            if (connectionTimeout < 0) {
                //TODO automatic default setting log creation
                return this;
            }

            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public LightSwitchConfig build() {
            return new LightSwitchConfig(this);
        }
    }


    public String getServerUrl() {
        return serverUrl;
    }

    public String getSdkKey() {
        return sdkKey;
    }

    public int getReconnectDelay() {
        return reconnectDelay;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }
}
