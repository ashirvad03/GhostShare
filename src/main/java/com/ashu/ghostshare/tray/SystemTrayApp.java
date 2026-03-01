package com.ashu.ghostshare.tray;

import com.ashu.ghostshare.server.SecureServer;
import com.ashu.ghostshare.utils.TunnelManager;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.UUID;

public class SystemTrayApp {
    private TrayIcon trayIcon;
    private MenuItem shareItem, stopItem;
    private SecureServer secureServer = new SecureServer();
    private TunnelManager tunnelManager = new TunnelManager();

    public void init() {
        if (!SystemTray.isSupported()) return;

        SystemTray tray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();

        shareItem = new MenuItem("Share File or Folder...");
        stopItem = new MenuItem("Stop Ghost Server");
        MenuItem exitItem = new MenuItem("Exit GhostShare");

        stopItem.setEnabled(false);

        shareItem.addActionListener(e -> openFileChooser());
        
        stopItem.addActionListener(e -> {
            secureServer.stopServer();
            tunnelManager.closeTunnel();
            resetUI();
            trayIcon.displayMessage("GhostShare", "Server Stopped Successfully", TrayIcon.MessageType.INFO);
        });

        exitItem.addActionListener(e -> {
            secureServer.stopServer();
            tunnelManager.closeTunnel();
            System.exit(0);
        });

        popup.add(shareItem);
        popup.add(stopItem);
        popup.addSeparator();
        popup.add(exitItem);

        Image image = createIcon("/assets/icon.png");
        trayIcon = new TrayIcon(image, "GhostShare - Active", popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) { e.printStackTrace(); }
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedPath = fileChooser.getSelectedFile();
            
            // 🛡️ Setup Security & Tunneling
            String token = UUID.randomUUID().toString().substring(0, 8);
            int port = 8443;

            new Thread(() -> {
                try {
                    // 1. UI update
                    shareItem.setEnabled(false);
                    stopItem.setEnabled(true);
                    trayIcon.displayMessage("Ghost Protocol", "Breaking Walls... Generating Global Link", TrayIcon.MessageType.INFO);

                    // 2. Open Tunnel
                    String globalLink = tunnelManager.openGlobalTunnel(port);
                    
                    if (globalLink != null) {
                        String finalUrl = globalLink + "/download?token=" + token;
                        
                        // 3. Copy to Clipboard (BCA Pro Move)
                        StringSelection selection = new StringSelection(finalUrl);
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
                        
                        // 4. Start Server
                        secureServer.startServer(port, selectedPath, token, this::resetUI);
                        
                        trayIcon.displayMessage("Success!", "Global Link Copied to Clipboard!", TrayIcon.MessageType.INFO);
                        System.out.println("[GHOST] Global Link: " + finalUrl);
                    } else {
                        trayIcon.displayMessage("Error", "ISP Bypass Failed. Using Local Mode.", TrayIcon.MessageType.WARNING);
                        // Fallback logic yahan daal sakte ho
                        resetUI();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resetUI();
                }
            }).start();
        }
    }

    public void resetUI() {
        shareItem.setEnabled(true);
        stopItem.setEnabled(false);
    }

    private Image createIcon(String path) {
        URL url = SystemTrayApp.class.getResource(path);
        if (url == null) return new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_RGB);
        return new ImageIcon(url).getImage();
    }
}