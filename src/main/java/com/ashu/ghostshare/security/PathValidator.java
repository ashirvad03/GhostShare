package com.ashu.ghostshare.security;

import java.io.File;
import java.io.IOException;

public class PathValidator {
    /**
     * Ye check karta hai ki share hone wali file safe location par hai ya nahi.
     */
    public static boolean isPathSafe(File fileToShare) {
        try {
            // File ka absolute path nikaalna
            String canonicalPath = fileToShare.getCanonicalPath();
            
            // Check: Kya file exist karti hai aur read ho sakti hai?
            if (!fileToShare.exists() || !fileToShare.canRead()) {
                return false;
            }

            // Check: Kya ye koi sensitive system directory toh nahi hai?
            String pathLower = canonicalPath.toLowerCase();
            if (pathLower.contains("c:\\windows") || pathLower.contains("c:\\users\\" + System.getProperty("user.name").toLowerCase() + "\\appdata")) {
                System.err.println("[SECURITY ALERT] Sensitive system path detected and blocked!");
                return false;
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}