package com.sofoste.thorshield;

import com.google.gson.*;
import com.sofoste.thorshield.collectors.*;
import com.sofoste.thorshield.models.*;
import com.sofoste.thorshield.reporting.Report;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DiagnosticTest {
    @ParameterizedTest
    @ValueSource(strings = {"normal", "warning", "access-denied"})
    void sharedContract(String fixture) throws IOException {
        JsonObject input;
        try (var stream = getClass().getResourceAsStream("/" + fixture + ".json")) {
            assertNotNull(stream);
            input = JsonParser.parseString(new String(stream.readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
        }
        var report = Report.from(List.of(WindowsCollector.decode("defender", input.get("defender").toString()),
            WindowsCollector.decode("firewall", input.get("firewall").toString())));
        assertEquals(input.get("expected_exit_code").getAsInt(), report.exitCode());
        assertEquals(input.getAsJsonArray("expected_severities").asList().stream().map(JsonElement::getAsString).toList(),
            report.findings().stream().map(Finding::severity).toList());
        JsonObject serialized = JsonParser.parseString(report.json()).getAsJsonObject();
        assertEquals("1.0", serialized.get("schema_version").getAsString());
        assertEquals(2, serialized.getAsJsonArray("observations").size());
        assertEquals(fixture.equals("access-denied") ? 1 : 0, report.errors().size());
    }
    @ParameterizedTest
    @ValueSource(strings = {"null", "{}", "broken", "{\"status\":\"success\",\"value\":{}}",
        "{\"status\":\"surprise\"}", "{\"status\":\"success\",\"value\":{\"Domain\":true,\"Private\":true,\"Public\":\"false\"}}",
        "{\"status\":\"success\",\"value\":{\"Domain\":true,\"Private\":true,\"Public\":null}}",
        "{\"status\":\"success\",\"value\":{\"Domain\":true}}"})
    void malformedDataNeverBecomesOk(String json) {
        Observation observation = WindowsCollector.decode("firewall", json);
        assertEquals("invalid_data", observation.status());
        assertEquals(2, Report.from(List.of(observation)).exitCode());
    }
    @Test void passiveDefenderRemainsUnknown() {
        var observation = WindowsCollector.decode("defender", """
            {"status":"success","value":{"antivirus_enabled":false,"realtime_enabled":false,"running_mode":"Passive"}}
            """);
        var report = Report.from(List.of(observation));
        assertEquals("unknown", report.findings().getFirst().severity());
        assertTrue(report.findings().getFirst().suggestion().contains("fournisseur"));
    }
    @ParameterizedTest @ValueSource(strings = {"timeout", "unavailable", "access_denied", "invalid_data"})
    void failuresStayExplicit(String status) {
        var report = Report.from(List.of(WindowsCollector.failed("defender", status)));
        assertEquals(2, report.exitCode());
        assertEquals(status, report.errors().getFirst().status());
    }
    @Test void boundedProcessOutput() {
        var result = new ProcessRunner().run(child("flood"), Duration.ofSeconds(5), 128);
        assertEquals("invalid_data", result.status());
        assertEquals("", result.output());
    }
    @Test void processTimeout() {
        long start = System.nanoTime();
        var result = new ProcessRunner().run(child("sleep"), Duration.ofMillis(250), 128);
        assertEquals("timeout", result.status());
        assertTrue(System.nanoTime() - start < Duration.ofSeconds(4).toNanos());
    }
    @Test void missingExecutableIsUnavailable() {
        assertEquals("unavailable", new ProcessRunner().run(List.of("nonexistent-thorshield-executable-391"),
            Duration.ofMillis(100), 128).status());
    }
    @Test void nonzeroExitIsUnavailable() {
        assertEquals("unavailable", new ProcessRunner().run(child("fail"), Duration.ofSeconds(5), 128).status());
    }
    private List<String> child(String mode) {
        return List.of(Path.of(System.getProperty("java.home"), "bin", "java").toString(), "-cp",
            System.getProperty("java.class.path"), Child.class.getName(), mode);
    }
    public static class Child {
        public static void main(String[] args) throws InterruptedException {
            if (args[0].equals("sleep")) Thread.sleep(15000);
            else if (args[0].equals("fail")) System.exit(7);
            else System.out.print("x".repeat(4096));
        }
    }
}
