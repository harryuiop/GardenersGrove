package nz.ac.canterbury.seng302.gardenersgrove.utility;

/**
 * Rate limiter to avoid overuse of API requests. Allows set of number of requests in a set timeframe.
 * Adapted from: https://dev.to/fromfullstack/top-10-java-interview-questions-about-rate-limiters-3ba7
 */
public class RateLimiter {
    private final long windowSizeSeconds;
    private final int maxRequestsPerWindow;
    private int currentRequests;

    private long lastWindowTimeMs = System.currentTimeMillis();

    private final long MS_IN_SECOND = 1000;

    /**
     * Initialize rate limiter.
     *
     * @param windowSizeSeconds Timeframe in seconds.
     * @param maxRequestsPerWindow Max requests allowed per window/timeframe.
     */
    public RateLimiter(int windowSizeSeconds, int maxRequestsPerWindow) {
        this.windowSizeSeconds = windowSizeSeconds;
        this.maxRequestsPerWindow = maxRequestsPerWindow;
    }

    /**
     * Check if request is within the maximum requests in the current window.
     * Update relevant information.
     *
     * @return If the requests is allowed or not.
     */
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();

        // Reset requests if the max requests for the window has been reached
        if (currentTime - lastWindowTimeMs > (windowSizeSeconds * MS_IN_SECOND)) {
            lastWindowTimeMs = currentTime;
            currentRequests = 0;
        }
        if (currentRequests < maxRequestsPerWindow) {
            currentRequests++;
            return true;
        }
        return false;
    }
}
