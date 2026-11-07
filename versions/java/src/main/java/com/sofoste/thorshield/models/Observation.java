package com.sofoste.thorshield.models;

import com.google.gson.JsonObject;
import java.time.Instant;

public record Observation(String id, String source, String collected_at, String status, JsonObject value) {
    public static Observation of(String id, String source, String status, JsonObject value) {
        return new Observation(id, source, Instant.now().toString(), status, value);
    }
}
