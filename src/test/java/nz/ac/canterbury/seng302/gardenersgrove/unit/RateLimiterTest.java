package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.utility.RateLimiter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class RateLimiterTest {

    private RateLimiter rateLimiter;

    private final int windowTimeSeconds = 10_000_000;

    @BeforeEach
    void setUpRateLimiter() {
        rateLimiter = new RateLimiter(windowTimeSeconds, 3);
    }

    @Test
    void allowRequestsInLimit() {
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertTrue(rateLimiter.allowRequest());
    }

    @Test
    void denyRequestOverLimit() {
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertFalse(rateLimiter.allowRequest());
    }

    @Test
    void allowRequestInNewWindow() {
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertTrue(rateLimiter.allowRequest());
        Assertions.assertFalse(rateLimiter.allowRequest());

        ReflectionTestUtils.setField(rateLimiter, "windowSizeSeconds", 0);
        Assertions.assertTrue(rateLimiter.allowRequest());
    }
}
