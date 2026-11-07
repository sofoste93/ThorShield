package com.sofoste.thorshield.collectors;

import com.google.gson.*;
import com.sofoste.thorshield.models.Observation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

public final class WindowsCollector {
    public static final Map<String, String> SOURCES = Map.of(
        "defender", "Get-MpComputerStatus",
        "firewall", "Get-NetFirewallProfile -PolicyStore ActiveStore");
    public List<Observation> collect() throws IOException {
        if (!System.getProperty("os.name").startsWith("Windows"))
            return List.of(failed("defender", "unavailable"), failed("firewall", "unavailable"));
        String root = System.getenv("SystemRoot");
        if (root == null) return List.of(failed("defender", "unavailable"), failed("firewall", "unavailable"));
        String script;
        try (var stream = WindowsCollector.class.getResourceAsStream("/collect.ps1")) {
            if (stream == null) throw new IOException("Collecteur embarqué manquant.");
            script = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
        String executable = Path.of(root, "System32", "WindowsPowerShell", "v1.0", "powershell.exe").toString();
        List<Observation> observations = new ArrayList<>();
        // Two independent processes preserve one result if the other source stalls.
        for (String id : List.of("defender", "firewall")) {
            String fixedScript = "$check = '" + id + "'\n" + script;
            String encoded = Base64.getEncoder().encodeToString(fixedScript.getBytes(StandardCharsets.UTF_16LE));
            var result = new ProcessRunner().run(List.of(executable, "-NoLogo", "-NoProfile", "-NonInteractive",
                "-EncodedCommand", encoded), Duration.ofSeconds(8), 65536);
            observations.add(result.status().equals("success") ? decode(id, result.output()) : failed(id, result.status()));
        }
        return List.copyOf(observations);
    }
    public static Observation failed(String id, String status) {
        return Observation.of(id, SOURCES.get(id), status, null);
    }
    public static Observation decode(String id, String json) {
        try {
            JsonObject envelope = new GsonBuilder().setStrictness(Strictness.STRICT).create().fromJson(json.strip(), JsonObject.class);
            String status = envelope.get("status").getAsString();
            if (!Set.of("success", "unavailable", "access_denied").contains(status)) throw new IllegalArgumentException();
            if (!status.equals("success")) return failed(id, status);
            JsonObject value = envelope.getAsJsonObject("value");
            if (id.equals("defender")) {
                requireBoolean(value, "antivirus_enabled"); requireBoolean(value, "realtime_enabled");
                if (value.size() != 3) throw new IllegalArgumentException();
                var mode = value.get("running_mode");
                if (mode == null || !mode.isJsonPrimitive() || !mode.getAsJsonPrimitive().isString()
                    || mode.getAsString().isBlank() || mode.getAsString().length() > 80) throw new IllegalArgumentException();
            } else if (id.equals("firewall")) {
                for (String profile : List.of("Domain", "Private", "Public")) requireBoolean(value, profile);
                if (value.size() != 3) throw new IllegalArgumentException();
            } else throw new IllegalArgumentException();
            return Observation.of(id, SOURCES.get(id), "success", value);
        } catch (RuntimeException e) {
            return failed(id, "invalid_data");
        }
    }
    private static void requireBoolean(JsonObject value, String key) {
        var element = value.get(key);
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            throw new IllegalArgumentException();
    }
}
