package com.sofoste.thorshield.reporting;

import com.google.gson.*;
import com.sofoste.thorshield.models.*;
import com.sofoste.thorshield.rules.Rules;
import java.time.Instant;
import java.util.List;

public record Report(String schema_version, String app_version, String implementation, String collected_at,
                     List<Observation> observations, List<Finding> findings, List<Error> errors) {
    public record Error(String observation_id, String status) {}
    public static Report from(List<Observation> observations) {
        return new Report("1.0", "0.1.0", "java", Instant.now().toString(), List.copyOf(observations),
            Rules.evaluate(observations), observations.stream().filter(o -> !o.status().equals("success"))
                .map(o -> new Error(o.id(), o.status())).toList());
    }
    public String json() { return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(this); }
    public int exitCode() {
        if (findings.stream().anyMatch(f -> f.severity().equals("unknown"))) return 2;
        return findings.stream().anyMatch(f -> f.severity().equals("warning")) ? 1 : 0;
    }
    public String summary() {
        StringBuilder text = new StringBuilder("ThorShield by Sofoste | Asgard Technology | 0.1.0\n");
        text.append("Diagnostic en lecture seule — ").append(collected_at).append("\n");
        for (Observation observation : observations)
            text.append("Source : ").append(observation.source()).append(" | ").append(observation.collected_at())
                .append(" | ").append(observation.status()).append("\n");
        for (Finding finding : findings)
            text.append("[").append(finding.severity()).append("] ").append(finding.message())
                .append("\n  ").append(finding.suggestion()).append("\n");
        text.append("Ces contrôles limités ne constituent pas un verdict global de sécurité.\n");
        return text.toString();
    }
}
