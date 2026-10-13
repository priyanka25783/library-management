package com.library;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String t = auth.login(body.get("username"), body.get("password"));
        if (t == null) return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        return ResponseEntity.ok(Map.of("token", t));
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(value = "Authorization", required = false) String h) {
        if (h != null && h.startsWith("Bearer ")) auth.logout(h.substring(7));
    }
}
