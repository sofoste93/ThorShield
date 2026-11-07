package com.sofoste.thorshield.cli;

import com.sofoste.thorshield.collectors.WindowsCollector;
import com.sofoste.thorshield.reporting.Report;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        System.exit(run(args));
    }
    static int run(String[] args) {
        if (args.length == 1 && args[0].equals("--help")) {
            System.out.println("ThorShield — Java 21+\nUsage : java -jar thorshield.jar [--json chemin.json] [--color auto|always|never]\n"
                + "Couleurs : vert=ok, jaune/orange=warning, cyan=unknown, rouge=erreur CLI/export.\n"
                + "Mode auto par défaut ; NO_COLOR désactive auto. always force la couleur.\n"
                + "Lecture seule, export facultatif sans écrasement. Codes : 0=contrôles OK, 1=à vérifier, 2=inconnu, 3=erreur CLI/export.");
            return 0;
        }
        String outputName = null;
        ColorMode mode = ColorMode.AUTO;
        boolean colorSeen = false;
        try {
            for (int i = 0; i < args.length; i++) {
                String option = args[i];
                if (i + 1 >= args.length) throw new IllegalArgumentException();
                String value = args[++i];
                if (option.equals("--json") && outputName == null && !value.isBlank() && !value.startsWith("--")) outputName = value;
                else if (option.equals("--color") && !colorSeen) {
                    mode = ColorMode.parse(value); colorSeen = true;
                } else throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            error("Arguments invalides. Utiliser --help.", false); return 3;
        }
        boolean color = mode.enabled(System.console() != null, System.getenv(), System.getProperty("os.name"));
        try {
            Path output = outputName == null ? null : Path.of(outputName);
            if (output != null && (Files.exists(output, LinkOption.NOFOLLOW_LINKS) || !Files.isDirectory(output.toAbsolutePath().getParent()))) {
                error("Export refusé : choisir un nouveau fichier dans un dossier existant.", color); return 3;
            }
            Report report = Report.from(new WindowsCollector().collect());
            System.out.print(report.summary(color));
            if (output != null) {
                Files.writeString(output, report.json() + "\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                System.out.println("Rapport JSON enregistré : " + output.toAbsolutePath());
            }
            return report.exitCode();
        } catch (IOException | InvalidPathException | SecurityException e) {
            error("Diagnostic ou export impossible. Vérifier le chemin et les droits d'accès.", color); return 3;
        }
    }
    private static void error(String message, boolean color) {
        System.err.println(com.sofoste.thorshield.reporting.StateColors.label("error", color) + " " + message);
    }
}
