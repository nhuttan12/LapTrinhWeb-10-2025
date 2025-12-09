package com.example.Utils;

import java.util.concurrent.ConcurrentHashMap;

public class TokenStorage {
    private static ConcurrentHashMap<String, Object> tokenMap = new ConcurrentHashMap<>();

    public static void put(String token, Object value) {
        tokenMap.put(token, value);
    }

    public static Object get(String token) {
        return tokenMap.get(token);
    }

    public static void remove(String token) {
        tokenMap.remove(token);
    }
}
