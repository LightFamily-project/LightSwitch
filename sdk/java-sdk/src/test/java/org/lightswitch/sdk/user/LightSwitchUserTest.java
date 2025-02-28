package org.lightswitch.sdk.user;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LightSwitchUserTest {

    @Test
    public void testUserAttributesAdditionAndRetrieval() {
        LightSwitchUser user = new LightSwitchUser("user1");
        user.addAttribute("region", "US").addAttribute("plan", "premium");

        assertEquals("US", user.getAttribute("region"));
        assertEquals("premium", user.getAttribute("plan"));
    }

    @Test
    public void testGetAttributesReturnsImmutableMap() {
        LightSwitchUser user = new LightSwitchUser("user1");
        user.addAttribute("region", "US");

        Map<String, String> attributes = user.getAttributes();
        assertThrows(UnsupportedOperationException.class, () -> attributes.put("plan", "basic"));
    }
}