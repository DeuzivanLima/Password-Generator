package io.github.deuzivanlima.passwordgenerator.core;

import java.security.SecureRandom;

public class RandomString {
    private final String SYMBOLS = "!@#$%^&*";
    private final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMBERS = "0123456789";
    private SecureRandom secure_random;
    private  int length;

    public RandomString(int length) {
        this.secure_random = new SecureRandom();

        this.length = length;
    }

    public String generate(boolean can_letters, boolean can_numbers, boolean can_symbols, boolean can_uppercase) {
        String chars_available = "", result = "";

        chars_available += can_letters ? this.LETTERS : "";
        chars_available += can_numbers ? this.NUMBERS : "";
        chars_available += can_symbols ? this.SYMBOLS : "";
        chars_available += can_uppercase ? this.LETTERS.toUpperCase() : "";

        result = randomize(chars_available);

        return result;
    }

    private String randomize(String chars_available) {
        char[] buffer = new char[this.length];
        String chars = mixString(chars_available);

        for(int i = 0; i < this.length; i++) {
            buffer[i] = chars.charAt(this.secure_random.nextInt(chars_available.length()));
        }

        return new String(buffer);
    }

    private String mixString(String text) {
        char[] result = text.toCharArray();

        for(int i = 0; i < text.length(); i++) {
            if(secure_random.nextInt(2) == 1) {
                int at = secure_random.nextInt(text.length());

                while(at == i) {
                    at = secure_random.nextInt(text.length());
                }

                char buffer = result[i];

                result[i] = result[at];
                result[at] = buffer;
            }
        }

        return new String(result);
    }
}
