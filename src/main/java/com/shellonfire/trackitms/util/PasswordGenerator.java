package com.shellonfire.trackitms.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static void main(String[] args) {
        int length = 10; // password length
//        String randomString = generateRandomString(length);
        String randomString = "admin";
        String encPass = new BCryptPasswordEncoder().encode(randomString);
        System.out.println("password: " + randomString);
        System.out.println("Enc password: " + encPass);

        System.out.println(new BCryptPasswordEncoder().matches("admin", encPass));
    }
}
