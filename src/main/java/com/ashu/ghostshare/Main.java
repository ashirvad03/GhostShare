package com.ashu.ghostshare;

import com.ashu.ghostshare.tray.SystemTrayApp;
import java.nio.file.*;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        // 1. Setup Auto-Run (Persistence)
        setupPersistence(); 
        
        // 2. Launch Tray Icon in Background
        javax.swing.SwingUtilities.invokeLater(() -> {
            new SystemTrayApp().init();
        });
        
        System.out.println("[GHOST] System active in background.");
    }

    private static void setupPersistence() {
        try {
            URI jarUri = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Path currentPath = Path.of(jarUri);
            
            // FIX: Kyunki hum EXE bana rahe hain, toh startup mein bhi .exe hi jayegi
            Path startupPath = Paths.get(System.getProperty("user.home"), 
                "AppData", "Roaming", "Microsoft", "Windows", "Start Menu", "Programs", "Startup", "GhostShare.exe");

            // Agar app Startup folder mein nahi hai, toh copy karo
            if (!currentPath.toString().contains("Startup")) {
                if (!Files.exists(startupPath)) {
                    Files.copy(currentPath, startupPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("[GHOST] System Persistence Established.");
                }
            }
        } catch (Exception e) {
            // Stealth mode
        }
    }
}