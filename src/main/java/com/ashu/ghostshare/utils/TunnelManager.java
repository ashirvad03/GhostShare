package com.ashu.ghostshare.utils;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class TunnelManager {
    private Process tunnelProcess;
    private File tempBinary;

    public String openGlobalTunnel(int localPort) {
        try {
            System.out.println("[GHOST-STEALTH] Deploying Bundled Bypass...");
            // Unique name taaki 'File in Use' error na aaye
            String uniqueName = "ghost_v" + UUID.randomUUID().toString().substring(0, 5) + ".exe";
            tempBinary = new File(System.getProperty("java.io.tmpdir"), uniqueName);
            
            try (InputStream is = getClass().getResourceAsStream("/bin/cloudflared.exe")) {
                if (is == null) throw new IOException("Binary missing in resources!");
                Files.copy(is, tempBinary.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            tempBinary.setExecutable(true);
            ProcessBuilder pb = new ProcessBuilder(tempBinary.getAbsolutePath(), "tunnel", "--url", "http://localhost:" + localPort);
            pb.redirectErrorStream(true);
            tunnelProcess = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(tunnelProcess.getInputStream()));
            String line;
            long timeout = System.currentTimeMillis() + 45000;

            while (System.currentTimeMillis() < timeout) {
                line = reader.readLine();
                if (line != null && line.contains(".trycloudflare.com")) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if (word.contains("trycloudflare.com")) return word.trim();
                    }
                }
            }
        } catch (Exception e) { System.err.println("Tunnel Error: " + e.getMessage()); }
        return null;
    }

    public void closeTunnel() {
        if (tunnelProcess != null) tunnelProcess.destroyForcibly();
        if (tempBinary != null) tempBinary.delete();
    }
}