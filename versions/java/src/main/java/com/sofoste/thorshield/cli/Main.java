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
            System.out.println("ThorShield — Java 21+\nUsage : java -jar thorshield.jar [--json chemin.json]\n"
                + "Lecture seule, export facultatif sans écrasement. Codes : 0=contrôles OK, 1=à vérifier, 2=inconnu, 3=erreur CLI/export.");
            return 0;
        }
        if (!(args.length == 0 || (args.length == 2 && args[0].equals("--json")))) {
            System.err.println("Arguments invalides. Utiliser --help."); return 3;
        }
        try {
            Path output = args.length == 2 ? Path.of(args[1]) : null;
            if (output != null && (Files.exists(output, LinkOption.NOFOLLOW_LINKS) || !Files.isDirectory(output.toAbsolutePath().getParent()))) {
                System.err.println("Export refusé : choisir un nouveau fichier dans un dossier existant."); return 3;
            }
            Report report = Report.from(new WindowsCollector().collect());
            System.out.print(report.summary());
            if (output != null) {
                Files.writeString(output, report.json() + "\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                System.out.println("Rapport JSON enregistré : " + output.toAbsolutePath());
            }
            return report.exitCode();
        } catch (IOException | InvalidPathException | SecurityException e) {
            System.err.println("Diagnostic ou export impossible. Vérifier le chemin et les droits d'accès."); return 3;
        }
    }
}
