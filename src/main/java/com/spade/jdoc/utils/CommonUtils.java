package com.spade.jdoc.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonUtils {

    public static Map<String, Object> cacheMap = new HashMap<>();

    public static String passwordHash(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static Boolean passwordVerify(String password, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
        return result.verified;
    }

    public static String getRandomStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Long getTime() {
        return System.currentTimeMillis() / 1000;
    }
}
