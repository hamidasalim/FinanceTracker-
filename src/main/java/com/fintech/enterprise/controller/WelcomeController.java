package com.fintech.enterprise.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller to handle the root path ("/") and prevent the "Whitelabel Error Page" (404)
 * when the user first accesses the application before the frontend is integrated.
 */
@RestController
public class WelcomeController {

    /**
     * Maps to the root URL and provides a simple status message.
     * When a UI is added, this controller will likely be removed or replaced by static resource serving.
     * @return A status message indicating the server is running.
     */
    @GetMapping("/")
    public String welcome() {
        return "FinTrack API Server is running. Access API endpoints (e.g., /api/users, /api/expenses) after authenticating.";
    }
}