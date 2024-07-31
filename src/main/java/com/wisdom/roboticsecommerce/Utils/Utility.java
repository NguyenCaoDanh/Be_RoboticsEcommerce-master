package com.wisdom.roboticsecommerce.Utils;


import jakarta.servlet.http.HttpServletRequest;

import java.util.Random;
import java.util.UUID;

public class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    public static String generateRandomToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    public static String generateRandomNumbers() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
