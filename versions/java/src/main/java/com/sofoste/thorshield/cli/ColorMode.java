package com.sofoste.thorshield.cli;

import java.util.Map;

enum ColorMode {
    AUTO, ALWAYS, NEVER;

    static ColorMode parse(String value) {
        return switch (value) {
            case "auto" -> AUTO;
            case "always" -> ALWAYS;
            case "never" -> NEVER;
            default -> throw new IllegalArgumentException("Mode couleur invalide.");
        };
    }

    boolean enabled(boolean terminal, Map<String, String> environment, String os) {
        if (this == ALWAYS) return true;
        if (this == NEVER || !terminal || !environment.getOrDefault("NO_COLOR", "").isEmpty()) return false;
        String term = environment.getOrDefault("TERM", "");
        if (term.equalsIgnoreCase("dumb")) return false;
        if (!os.startsWith("Windows")) return !term.isBlank();
        return !environment.getOrDefault("WT_SESSION", "").isBlank()
            || environment.containsKey("ANSICON")
            || environment.getOrDefault("ConEmuANSI", "").equalsIgnoreCase("ON")
            || term.startsWith("xterm") || term.startsWith("screen");
    }
}
