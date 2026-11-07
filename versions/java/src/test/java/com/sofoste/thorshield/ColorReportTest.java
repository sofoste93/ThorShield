package com.sofoste.thorshield;

import com.sofoste.thorshield.collectors.WindowsCollector;
import com.sofoste.thorshield.reporting.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColorReportTest {
    @Test void colorNeverChangesReportMeaningOrJson() {
        Report report = Report.from(List.of(WindowsCollector.failed("defender", "access_denied")));
        String json = report.json();
        String colored = report.summary(true);
        assertTrue(colored.contains("\u001b[96m[unknown]\u001b[0m"));
        assertEquals(report.summary(), colored.replaceAll("\u001b\\[[0-9;]*m", ""));
        assertFalse(report.summary().contains("\u001b"));
        assertEquals(json, report.json());
        assertFalse(report.json().contains("\\u001b"));
        assertEquals(2, report.exitCode());
    }
    @Test void eachStateKeepsLabelAndResetsColor() {
        for (String state : List.of("ok", "warning", "unknown", "error")) {
            assertEquals("[" + state + "]", StateColors.label(state, false));
            assertTrue(StateColors.label(state, true).endsWith("\u001b[0m"));
        }
        assertTrue(StateColors.label("ok", true).startsWith("\u001b[92m"));
        assertTrue(StateColors.label("warning", true).startsWith("\u001b[93m"));
        assertTrue(StateColors.label("error", true).startsWith("\u001b[91m"));
    }
}
