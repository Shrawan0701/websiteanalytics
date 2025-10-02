package com.analytics.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackingSnippetController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/tracking.js", produces = "application/javascript")
    public ResponseEntity<String> getTrackingScript() {
        String script = "(function(){ ... })();";
        return ResponseEntity.ok()
                .header("Content-Type", "application/javascript")
                .body(script);
    }

}
