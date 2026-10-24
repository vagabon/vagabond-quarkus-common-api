package org.vagabond.engine.filter;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import io.quarkus.logging.Log;

@Provider
public class FilterConfiguration implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log();
    }

    private void log() {
        Log.debugf("Called on %s", Thread.currentThread());
    }
}