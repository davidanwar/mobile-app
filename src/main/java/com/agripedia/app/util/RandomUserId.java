package com.agripedia.app.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class RandomUserId {

    private Random RANDOM = new SecureRandom();
    private String ALPHABET = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private final int ITERATIONS = 100;
    private final int KEY_LENGTH = 256;

    public String generateId(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++ ) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }


}
