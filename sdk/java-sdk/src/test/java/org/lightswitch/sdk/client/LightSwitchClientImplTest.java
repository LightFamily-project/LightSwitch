package org.lightswitch.sdk.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lightswitch.sdk.exception.LightSwitchCastException;
import org.lightswitch.sdk.user.LightSwitchUser;

import static org.junit.jupiter.api.Assertions.*;

class LightSwitchClientImplTest {

    private LightSwitchClient client;

    @BeforeEach
    public void setUp() {
        client = new LightSwitchClientImpl();
    }

    @Test
    public void testBooleanFlag_withMatchingCondition() {
        LightSwitchUser user = new LightSwitchUser("user1").addAttribute("region", "US");
        boolean flagValue = client.getBooleanFlag("new-dashboard", false, user);
        assertTrue(flagValue, "US user must be returned TRUE flag for new-dashboard");
    }

    @Test
    public void testBooleanFlag_withoutMatchingCondition() {
        LightSwitchUser user = new LightSwitchUser("user2").addAttribute("region", "EU");
        boolean flagValue = client.getBooleanFlag("new-dashboard", false, user);
        assertFalse(flagValue, "EU user must be returned FALSE flag for new-dashboard");
    }

    @Test
    public void testIntFlag_discount_US() {
        LightSwitchUser user = new LightSwitchUser("user1").addAttribute("region", "US");
        int discount = client.getIntFlag("discount", 0, user);
        assertEquals(100, discount, "US user must be returned 100 flag for discount rate");
    }

    @Test
    public void testIntFlag_discount_EU() {
        LightSwitchUser user = new LightSwitchUser("user2").addAttribute("region", "EU");
        int discount = client.getIntFlag("discount", 0, user);
        assertEquals(50, discount, "EU user must be returned 50 flag for discount rate");
    }

    @Test
    public void testIntFlag_discount_default() {
        LightSwitchUser user = new LightSwitchUser("user3");
        int discount = client.getIntFlag("discount", 0, user);
        assertEquals(0, discount, "It must be return 0 which is default flag");
    }

    @Test
    public void testCastException_onWrongType() {
        LightSwitchUser user = new LightSwitchUser("user1").addAttribute("region", "US");
        Exception exception = assertThrows(LightSwitchCastException.class, () -> {
            client.getBooleanFlag("discount", false, user);
        });
        String expectedMessage = "expected type Boolean";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testIsEnabled() {
        LightSwitchUser user = new LightSwitchUser("user1").addAttribute("region", "US");
        boolean enabled = client.isEnabled("new-dashboard", user);
        assertTrue(enabled);
    }

    @Test
    public void testDestroy_clearsFlags() {
        LightSwitchUser user = new LightSwitchUser("user1").addAttribute("region", "US");
        client.destroy();
        boolean flagValue = client.getBooleanFlag("new-dashboard", false, user);
        assertFalse(flagValue, "It must return default flag after destroy");
    }
}