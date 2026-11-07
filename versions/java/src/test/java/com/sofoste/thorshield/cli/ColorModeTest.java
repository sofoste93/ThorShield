package com.sofoste.thorshield.cli;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ColorModeTest {
    @Test void automaticColorRequiresTerminal() {
        assertFalse(ColorMode.AUTO.enabled(false, Map.of("WT_SESSION", "demo"), "Windows 11"));
    }
    @Test void windowsTerminalSupportsAuto() {
        assertTrue(ColorMode.AUTO.enabled(true, Map.of("WT_SESSION", "demo"), "Windows 11"));
    }
    @Test void noColorDisablesAuto() {
        assertFalse(ColorMode.AUTO.enabled(true, Map.of("WT_SESSION", "demo", "NO_COLOR", "1"), "Windows 11"));
    }
    @Test void emptyNoColorDoesNotDisableAuto() {
        assertTrue(ColorMode.AUTO.enabled(true, Map.of("TERM", "xterm", "NO_COLOR", ""), "Linux"));
    }
    @Test void dumbAndUnrecognizedTerminalsStayPlain() {
        assertFalse(ColorMode.AUTO.enabled(true, Map.of("TERM", "dumb", "WT_SESSION", "demo"), "Windows 11"));
        assertFalse(ColorMode.AUTO.enabled(true, Map.of(), "Windows 11"));
    }
    @Test void explicitModesOverrideEnvironment() {
        assertTrue(ColorMode.ALWAYS.enabled(false, Map.of("NO_COLOR", "1"), "Windows 11"));
        assertFalse(ColorMode.NEVER.enabled(true, Map.of("WT_SESSION", "demo"), "Windows 11"));
    }
    @Test void invalidModeRejected() {
        assertThrows(IllegalArgumentException.class, () -> ColorMode.parse("sometimes"));
    }
}
