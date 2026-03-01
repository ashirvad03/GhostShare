package com.ashu.ghostshare.server;

import java.util.Timer;
import java.util.TimerTask;

public class AutoKillSwitch {
    private Timer timer;
    private final SecureServer server;

    public AutoKillSwitch(SecureServer server) {
        this.server = server;
        this.timer = new Timer(true); // Daemon thread taaki app exit hote hi ye bhi band ho jaye
    }

    /**
     * Agar server start hone ke 10 minute tak koi download nahi hua, 
     * toh ye automatically server ko band kar dega.
     */
    public void startInactivityTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("[GHOST] Inactivity timeout reached. Shutting down vault...");
                server.stopServer();
            }
        }, 600000); // 10 minutes (in milliseconds)
    }

    /**
     * Kisi bhi active timer ko cancel karne ke liye.
     */
    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            System.out.println("[GHOST] Auto-Kill timer reset.");
        }
    }
}