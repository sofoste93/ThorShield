package com.sofoste.thorshield.rules;

import com.sofoste.thorshield.models.*;
import java.util.*;

public final class Rules {
    private Rules() {}
    public static List<Finding> evaluate(List<Observation> observations) {
        List<Finding> findings = new ArrayList<>();
        for (Observation observation : observations) {
            String id = observation.id();
            if (!observation.status().equals("success")) {
                findings.add(finding(id, "unknown", "Lecture impossible : " + observation.status() + ".",
                    "Vérifier ce contrôle dans Sécurité Windows ; aucune conclusion sur la protection."));
            } else if (id.equals("defender")) {
                var value = observation.value();
                boolean normal = "Normal".equalsIgnoreCase(value.get("running_mode").getAsString());
                boolean enabled = value.get("antivirus_enabled").getAsBoolean() && value.get("realtime_enabled").getAsBoolean();
                findings.add(finding(id, normal ? (enabled ? "ok" : "warning") : "unknown",
                    normal && enabled ? "Defender et sa protection en temps réel sont actifs."
                        : normal ? "Au moins une protection Defender est inactive."
                        : "Defender ne déclare pas le mode Normal ; contexte antivirus à vérifier.",
                    normal && enabled ? "Ce contrôle ne garantit pas l'absence de menace."
                        : "Consulter Sécurité Windows et le fournisseur antivirus actif. Aucun autre antivirus n'est évalué ici."));
            } else if (id.equals("firewall")) {
                for (String profile : List.of("Domain", "Private", "Public")) {
                    boolean enabled = observation.value().get(profile).getAsBoolean();
                    findings.add(new Finding("firewall." + profile.toLowerCase(Locale.ROOT), enabled ? "ok" : "warning",
                        List.of(id), "Profil " + profile + " : pare-feu " + (enabled ? "activé." : "désactivé."),
                        "Configuration effective du profil ; ce profil n'est pas nécessairement celui du réseau actuel."));
                }
            }
        }
        return List.copyOf(findings);
    }
    private static Finding finding(String id, String severity, String message, String suggestion) {
        return new Finding(id + ".state", severity, List.of(id), message, suggestion);
    }
}
