package com.sofoste.thorshield.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @TempDir Path directory;
    @Test void existingReportIsNeverOverwritten() throws Exception {
        Path report = directory.resolve("rapport.json");
        Files.writeString(report, "original");
        assertEquals(3, Main.run(new String[]{"--json", report.toString()}));
        assertEquals("original", Files.readString(report));
    }
    @Test void invalidArgumentsDoNotStartCollection() {
        assertEquals(3, Main.run(new String[]{"--json"}));
        assertEquals(3, Main.run(new String[]{"--unknown"}));
    }
    @Test void missingParentIsRejected() {
        assertEquals(3, Main.run(new String[]{"--json", directory.resolve("absent/report.json").toString()}));
    }
    @Test void helpSucceedsWithoutWindowsCollection() {
        assertEquals(0, Main.run(new String[]{"--help"}));
    }
}
