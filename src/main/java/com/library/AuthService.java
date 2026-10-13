package com.library;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private static final long TTL_MS = 8L * 60 * 60 * 1000; // 8 hours
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();
    @Value("${admin.username}") private String user;
    @Value("${admin.password}") private String pass;

    public String login(String u, String p) {
        if (user.equals(u) && pass.equals(p)) {
            String t = UUID.randomUUID().toString();
            tokens.put(t, System.currentTimeMillis() + TTL_MS);
            return t;
        }
        return null;
    }
    public boolean valid(String t) {
        Long exp = t == null ? null : tokens.get(t);
        if (exp == null) return false;
        if (exp < System.currentTimeMillis()) { tokens.remove(t); return false; }
        return true;
    }
    public void logout(String t) { if (t != null) tokens.remove(t); }
}
