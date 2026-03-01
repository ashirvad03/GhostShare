package com.ashu.ghostshare.server;

import io.javalin.Javalin;
import java.io.File;

public class SecureServer {
    private Javalin app;

    public void startServer(int port, File fileToSend, String token, Runnable onStop) {
        app = Javalin.create().start(port);

        app.get("/download", ctx -> {
            String userToken = ctx.queryParam("token");
            if (token.equals(userToken)) {
                ctx.result(new java.io.FileInputStream(fileToSend))
                   .header("Content-Disposition", "attachment; filename=\"" + fileToSend.getName() + "\"");
                
                // Smart Kill Switch: Download shuru hone ke baad 1 ghante ka time
                startTimeout(fileToSend, onStop);
            } else {
                ctx.status(403).result("Unauthorized: Ghost Access Denied.");
            }
        });
    }

    private void startTimeout(File fileToSend, Runnable onStop) {
        new Thread(() -> {
            try {
                System.out.println("[GHOST] 10GB Buffer Active. Vault open for 60 mins.");
                Thread.sleep(3600000); // 1 Hour for large transfers
                stopServer();
                if (fileToSend.getName().contains("_GhostArchive.zip")) fileToSend.delete();
                onStop.run();
            } catch (Exception e) { }
        }).start();
    }

    public void stopServer() {
        if (app != null) app.stop();
    }
}