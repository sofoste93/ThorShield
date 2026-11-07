package com.sofoste.thorshield.collectors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

/** Bounded output and lifetime; no shell expansion of arguments. */
public final class ProcessRunner {
    public record Result(String status, String output) {}
    public Result run(List<String> command, Duration timeout, int limit) {
        Process process = null;
        ExecutorService reader = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "thorshield-output"); thread.setDaemon(true); return thread;
        });
        try {
            process = new ProcessBuilder(command).redirectErrorStream(true).start();
            Process child = process;
            Future<byte[]> output = reader.submit(() -> {
                byte[] bytes = child.getInputStream().readNBytes(limit + 1);
                if (bytes.length > limit) stop(child);
                return bytes;
            });
            long deadline = System.nanoTime() + timeout.toNanos();
            if (!process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS)) return new Result("timeout", "");
            byte[] bytes = output.get(Math.max(1, deadline - System.nanoTime()), TimeUnit.NANOSECONDS);
            if (bytes.length > limit) return new Result("invalid_data", "");
            if (process.exitValue() != 0) return new Result("unavailable", "");
            return new Result("success", new String(bytes, StandardCharsets.UTF_8));
        } catch (TimeoutException e) {
            return new Result("timeout", "");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); return new Result("unavailable", "");
        } catch (IOException | ExecutionException e) {
            return new Result("unavailable", "");
        } finally {
            if (process != null) {
                stop(process);
                try { process.getInputStream().close(); } catch (IOException ignored) { }
            }
            reader.shutdownNow();
        }
    }
    private static void stop(Process process) {
        process.descendants().forEach(ProcessHandle::destroyForcibly);
        if (process.isAlive()) process.destroyForcibly();
    }
}
