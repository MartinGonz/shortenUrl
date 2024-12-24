package com.example.shortUrl.controller;

import com.example.shortUrl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private JwtUtils jwtUtil;

    @PostMapping("/authenticate")
        public ResponseEntity<?> authenticate(@RequestParam String username, @RequestParam String password) {
        // Hardcoded user credentials,
        if ("user".equals(username) && "password".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
