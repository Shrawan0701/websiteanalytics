package com.analytics.controller;

import com.analytics.dto.EventRequest;
import com.analytics.dto.MessageResponse;
import com.analytics.service.EventService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/track")
public class TrackingController {
    private static final Logger logger = LoggerFactory.getLogger(TrackingController.class);

    @Autowired
    private EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<?> trackEvent(@Valid @RequestBody EventRequest request, HttpServletRequest servletRequest) {
        try {
            String ip = servletRequest.getRemoteAddr();
            eventService.trackEvent(request, ip);
            logger.debug("Tracked event: {}", request.getEventType());
            return ResponseEntity.ok(new MessageResponse("Event tracked successfully", true));
        } catch (Exception ex) {
            logger.error("Tracking failed: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to track event", false));
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok(new MessageResponse("Analytics tracking service running", true));
    }

    @PostMapping("/batch")
    public ResponseEntity<?> trackBatch(@RequestBody EventRequest[] requests, HttpServletRequest servletRequest) {
        String ip = servletRequest.getRemoteAddr();
        eventService.trackEvents(requests, ip);
        logger.debug("Tracked batch of {} events", requests.length);
        return ResponseEntity.ok(new MessageResponse("Batch events tracked", true));
    }
}
