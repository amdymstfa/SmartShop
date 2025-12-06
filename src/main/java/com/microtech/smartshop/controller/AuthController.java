package com.microtech.smartshop.controller ;

import com.microtech.smartshop.dto.request.LoginRequest;
import com.microtech.smartshop.entity.User;
import com.microtech.smartshop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService ;

    /**
     * POST /api/auth/login
     * Authenticate user
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate a user with username and password")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        User user = authService.login(request.getUsername(), request.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        log.info("User {} logged in successfully", user.getUsername());

        return ResponseEntity.ok(response);
    }

    /**
     * Post /api/auth/logout
     * Disconnect the current user
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout current user")
    public ResponseEntity<Map<String, Object>> logout(){
        log.info("Logout request");

        authService.logout();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logout successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Get /api/auth/me
     * Retrieves the currently logged-in user
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get currently authenticated user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(){
        User user = authService.getCurrentUser();

        if (user == null){
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }
}