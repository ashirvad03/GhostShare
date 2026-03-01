package com.ashu.ghostshare.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class TokenManager {
    /**
     * Ek ekdum unique aur random security token generate karta hai.
     */
    public static String generateSecureToken() {
        // SecureRandom standard Random class se zyada hacker-proof hoti hai
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        
        // Base64 URL Safe encoding taaki link mein koi gadbad na ho
        String base64Token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        
        // UUID aur Base64 ko milakar ek bawal token banta hai
        return UUID.randomUUID().toString().substring(0, 8) + "-" + base64Token;
    }
}