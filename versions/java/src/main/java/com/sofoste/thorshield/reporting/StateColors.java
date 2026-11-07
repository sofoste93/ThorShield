package com.sofoste.thorshield.reporting;

/** Shared terminal and preview palette. Labels remain visible without color. */
public final class StateColors {
    private StateColors() {}
    public static String hex(String state) {
        return switch (state) {
            case "ok", "success" -> "#4ade80";
            case "warning" -> "#fbbf24";
            case "unknown", "unavailable", "access_denied", "timeout", "invalid_data" -> "#67e8f9";
            case "error" -> "#f87171";
            default -> "#e4ecf7";
        };
    }
    public static String label(String state, boolean enabled) {
        String label = "[" + state + "]";
        if (!enabled) return label;
        // Standard ANSI colors remain compatible with 16-color terminals.
        String code = switch (state) {
            case "ok", "success" -> "92";
            case "warning" -> "93";
            case "unknown", "unavailable", "access_denied", "timeout", "invalid_data" -> "96";
            case "error" -> "91";
            default -> "0";
        };
        return "\u001b[" + code + "m" + label + "\u001b[0m";
    }
}
