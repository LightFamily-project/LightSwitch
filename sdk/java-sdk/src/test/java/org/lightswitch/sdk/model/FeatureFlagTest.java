package org.lightswitch.sdk.model;

import org.junit.jupiter.api.Test;
import org.lightswitch.sdk.user.LightSwitchUser;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeatureFlagTest {

    @Test
    public void testEvaluate_returnsVariantValue_whenConditionMatches() {
        FeatureFlag flag = new FeatureFlag(
                "discount",
                0,
                Map.of(
                        Map.of("region", "US"), 100,
                        Map.of("region", "EU"), 50
                )
        );

        LightSwitchUser userUS = new LightSwitchUser("user1").addAttribute("region", "US");
        Object result = flag.evaluate(userUS);
        assertEquals(100, result);

        LightSwitchUser userEU = new LightSwitchUser("user2").addAttribute("region", "EU");
        result = flag.evaluate(userEU);
        assertEquals(50, result);
    }

    @Test
    public void testEvaluate_returnsDefaultValue_whenNoConditionMatches() {
        FeatureFlag flag = new FeatureFlag(
                "discount",
                0,
                Map.of(
                        Map.of("region", "US"), 100
                )
        );
        LightSwitchUser user = new LightSwitchUser("user3").addAttribute("region", "EU");
        Object result = flag.evaluate(user);
        assertEquals(0, result);
    }

    @Test
    public void testEvaluate_returnsDefaultValue_whenNoConditionsDefined() {
        FeatureFlag flag = new FeatureFlag("new-dashboard", false, null);
        LightSwitchUser user = new LightSwitchUser("user1");
        Object result = flag.evaluate(user);
        assertEquals(false, result);
    }

}