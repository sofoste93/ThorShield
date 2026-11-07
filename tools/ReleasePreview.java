import com.google.gson.*;
import com.sofoste.thorshield.collectors.WindowsCollector;
import com.sofoste.thorshield.models.Observation;
import com.sofoste.thorshield.reporting.Report;
import com.sofoste.thorshield.reporting.StateColors;
import java.nio.file.*;
import java.util.*;

/** Render real report formatting from synthetic fixtures; never collects PC data. */
public class ReleasePreview {
    public static void main(String[] args) throws Exception {
        Path root = Path.of(args[0]);
        Path output = root.resolve("versions/java/target/release-previews");
        Files.createDirectories(output);
        for (String scenario : List.of("normal", "warning", "access-denied")) {
            JsonObject fixture = JsonParser.parseString(Files.readString(root.resolve("shared/fixtures/" + scenario + ".json"))).getAsJsonObject();
            List<Observation> observations = new ArrayList<>();
            String time = "2026-01-01T12:00:00Z";
            for (String id : List.of("defender", "firewall")) {
                Observation decoded = WindowsCollector.decode(id, fixture.get(id).toString());
                observations.add(new Observation(id, decoded.source(), time, decoded.status(), decoded.value()));
            }
            Report generated = Report.from(observations);
            Report report = new Report(generated.schema_version(), generated.app_version(), generated.implementation(),
                time, generated.observations(), generated.findings(), generated.errors());
            if (report.exitCode() != fixture.get("expected_exit_code").getAsInt()) throw new IllegalStateException("Fixture mismatch");
            String text = report.summary().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
            for (String state : List.of("ok", "warning", "unknown", "success", "access_denied"))
                text = text.replace("[" + state + "]", "<span style=\"color:" + StateColors.hex(state)
                    + ";font-weight:700\">[" + state + "]</span>");
            String html = """
                <!doctype html><html lang="en"><meta charset="utf-8"><title>ThorShield release preview</title>
                <style>
                *{box-sizing:border-box}body{margin:0;padding:44px;background:#0b1220;color:#e4ecf7;font-family:Segoe UI,sans-serif}
                .brand{color:#65d9d1;letter-spacing:3px;font-size:16px}h1{font-size:34px;margin:14px 0}
                .badge{display:inline-block;background:#25384c;border:1px solid #597491;border-radius:8px;padding:10px 16px;margin-bottom:24px}
                pre{background:#121c2b;border:1px solid #30445d;border-radius:14px;padding:28px;font:17px/1.6 Consolas,monospace;white-space:pre-wrap;overflow-wrap:anywhere;margin:0}
                footer{color:#a8bdd5;font-size:16px;line-height:1.6;margin-top:20px}
                </style><div class="brand">ASGARD TECHNOLOGY · SOFOSTE</div>
                <h1>ThorShield VERSION · SCENARIO</h1>
                <div class="badge">SYNTHETIC DEMO / DÉMONSTRATION FICTIVE</div>
                <pre>OUTPUT</pre>
                <footer>CLI output rendered for release documentation · Sortie CLI mise en page pour la release.<br>
                Not an application GUI. No real PC data. / Aucune interface graphique ni donnée réelle du PC.<br>
                Fixture: SCENARIO · Exit code / Code de sortie: EXIT</footer></html>
                """.replace("VERSION", report.app_version()).replace("SCENARIO", scenario).replace("OUTPUT", text).replace("EXIT", Integer.toString(report.exitCode()));
            Files.writeString(output.resolve(scenario + ".html"), html);
        }
    }
}
