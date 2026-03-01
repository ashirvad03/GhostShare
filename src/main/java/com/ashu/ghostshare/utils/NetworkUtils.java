package com.ashu.ghostshare.utils;

import java.net.*;
import java.util.*;

public class NetworkUtils {
    /**
     * System ke asli Local IPv4 address ko nikaalne ke liye logic.
     */
    public static String getLocalIP() {
        try {
            // Saare network interfaces (Wi-Fi, Ethernet, Virtual) ki list fetch karna
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                
                // Loopback, down interfaces aur virtual adapters ko filter karna
                if (ni.isLoopback() || !ni.isUp() || 
                    ni.getDisplayName().toLowerCase().contains("virtual") || 
                    ni.getDisplayName().toLowerCase().contains("vmware")) {
                    continue;
                }

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    
                    // Sirf IPv4 address (jaise 192.168.x.x) pick karna hai
                    if (addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Network interfaces read karne mein error: " + e.getMessage());
        }
        return "127.0.0.1"; // Default fallback agar kuch na mile
    }
}