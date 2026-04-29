package org.vagabond.engine.filter.rate_limit;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;

@Provider
@Priority(1)
public class RateLimitFilterConfiguration implements ContainerRequestFilter {

    private static final int MAX_REQUESTS = 100;
    private static final long WINDOW_MS = 60_000;

    @ConfigProperty(name = "rate.limit.enabled", defaultValue = "true")
    private boolean enabled;

    @ConfigProperty(name = "rate.limit.whitelist", defaultValue = "127.0.0.1")
    private List<String> whitelist;

    private final Cache<String, List<Long>> requests = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES).maximumSize(10_000).build();

    @Override
    public void filter(ContainerRequestContext ctx) {
        if (!enabled) {
            return;
        }

        String ip = getClientIp(ctx);

        if (whitelist.contains(ip)) {
            return;
        }

        long now = System.currentTimeMillis();
        Log.infof("Received request from %s — %s %s", ip, ctx.getMethod(), ctx.getUriInfo().getRequestUri());

        List<Long> timestamps = requests.get(ip, k -> new CopyOnWriteArrayList<>());
        timestamps.removeIf(t -> now - t > WINDOW_MS);
        timestamps.add(now);

        if (timestamps.size() > MAX_REQUESTS) {
            Log.errorf("LIMIIIIIIIIIIT TOO_MANY_REQUESTS %s at %s", ip, now);
            ctx.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity("{\"error\": \"Too many requests, slow down.\"}").header("Retry-After", "1000")
                    .header("Content-Type", "application/json").build());
        }
    }

    private String getClientIp(ContainerRequestContext ctx) {
        String forwarded = ctx.getHeaderString("X-Forwarded-For");
        if (forwarded != null)
            return forwarded.split(",")[0].trim();

        String realIp = ctx.getHeaderString("X-Real-IP");
        if (realIp != null)
            return realIp.trim();

        return "unknown";

    }
}