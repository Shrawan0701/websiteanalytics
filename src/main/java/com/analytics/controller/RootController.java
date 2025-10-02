package com.analytics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RootController {

    // Respond to root GET requests
    @GetMapping("/")
    public String home() {
        return "API is running";
    }

    // Catch all OPTIONS requests (preflight)
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
