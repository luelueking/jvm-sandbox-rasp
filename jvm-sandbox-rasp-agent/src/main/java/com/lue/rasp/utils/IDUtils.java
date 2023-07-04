package com.lue.rasp.utils;

import java.util.UUID;

public class IDUtils {
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
