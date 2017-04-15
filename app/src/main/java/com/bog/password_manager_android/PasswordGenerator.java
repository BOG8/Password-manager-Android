package com.bog.password_manager_android;

import java.security.SecureRandom;

/**
 * Created by Олег on 15.04.2017.
 */

class PasswordGenerator {
    private final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    String generatePassword(int numberOfChars) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < numberOfChars; i++) {
            int index = new SecureRandom().nextInt(62);
            password.append(str.charAt(index));
        }
        return password.toString();
    }
}
