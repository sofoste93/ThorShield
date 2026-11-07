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
    @Test void invalidColorAndDuplicateOptionsAreRejected() {
        assertEquals(3, Main.run(new String[]{"--color", "sometimes"}));
        assertEquals(3, Main.run(new String[]{"--color"}));
        assertEquals(3, Main.run(new String[]{"--color", "never", "--color", "always"}));
        assertEquals(3, Main.run(new String[]{"--json", "--color"}));
        assertEquals(3, Main.run(new String[]{"--json", "a.json", "--json", "b.json"}));
    }
    @Test void colorAndExportOptionsCombineInEitherOrder() throws Exception {
        Path file = directory.resolve("existing.json");
        Files.writeString(file, "original");
        var originalErr = System.err;
        var buffer = new java.io.ByteArrayOutputStream();
        try (var capture = new java.io.PrintStream(buffer, true, java.nio.charset.StandardCharsets.UTF_8)) {
            System.setErr(capture);
            assertEquals(3, Main.run(new String[]{"--color", "always", "--json", file.toString()}));
            assertTrue(buffer.toString(java.nio.charset.StandardCharsets.UTF_8).contains("\u001b[91m[error]\u001b[0m"));
            buffer.reset();
            assertEquals(3, Main.run(new String[]{"--json", file.toString(), "--color", "never"}));
            assertFalse(buffer.toString(java.nio.charset.StandardCharsets.UTF_8).contains("\u001b"));
        } finally { System.setErr(originalErr); }
        assertEquals("original", Files.readString(file));
    }
}
