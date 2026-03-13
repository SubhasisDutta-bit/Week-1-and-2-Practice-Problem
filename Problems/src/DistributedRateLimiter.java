import java.util.*;

public class DistributedRateLimiter {

    class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefillTime;

        TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    HashMap<String, TokenBucket> clients = new HashMap<>();
    int LIMIT = 1000;
    long WINDOW = 3600 * 1000;

    public synchronized String checkRateLimit(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            bucket = new TokenBucket(LIMIT);
            clients.put(clientId, bucket);
        }

        long now = System.currentTimeMillis();

        if (now - bucket.lastRefillTime >= WINDOW) {
            bucket.tokens = bucket.maxTokens;
            bucket.lastRefillTime = now;
        }

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            long retry = (WINDOW - (now - bucket.lastRefillTime)) / 1000;
            return "Denied (0 requests remaining, retry after " + retry + "s)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            return "{used: 0, limit: " + LIMIT + "}";
        }

        int used = bucket.maxTokens - bucket.tokens;
        long reset = (bucket.lastRefillTime + WINDOW) / 1000;

        return "{used: " + used + ", limit: " + LIMIT + ", reset: " + reset + "}";
    }

    public static void main(String[] args) {

        DistributedRateLimiter limiter = new DistributedRateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        for (int i = 0; i < 998; i++) {
            limiter.checkRateLimit("abc123");
        }

        System.out.println(limiter.checkRateLimit("abc123"));

        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}