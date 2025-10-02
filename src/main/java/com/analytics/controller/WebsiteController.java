package com.analytics.controller;

import com.analytics.dto.MessageResponse;
import com.analytics.dto.WebsiteRequest;
import com.analytics.entity.User;
import com.analytics.entity.Website;
import com.analytics.repository.UserRepository;
import com.analytics.repository.WebsiteRepository;
import com.analytics.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/websites")
public class WebsiteController {
    private static final Logger logger = LoggerFactory.getLogger(WebsiteController.class);

    @Autowired
    private WebsiteRepository websiteRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Website>> getAllWebsites(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Website> websites = websiteRepository.findByUserId(userDetails.getId());
        logger.info("Retrieved websites for user {}", userDetails.getUsername());
        return ResponseEntity.ok(websites);
    }

    @PostMapping
    public ResponseEntity<?> createWebsite(@Valid @RequestBody WebsiteRequest websiteRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found", false));
        }
        Website website = new Website(websiteRequest.getName(), websiteRequest.getDomain(), userOptional.get());
        Website saved = websiteRepository.save(website);
        logger.info("Website created: {}", websiteRequest.getName());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{websiteId}")
    public ResponseEntity<?> getWebsite(@PathVariable UUID websiteId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Website> websiteOpt = websiteRepository.findByWebsiteIdAndUserId(websiteId, userDetails.getId());
        if (websiteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(websiteOpt.get());
    }

    @PutMapping("/{websiteId}")
    public ResponseEntity<?> updateWebsite(@PathVariable UUID websiteId, @Valid @RequestBody WebsiteRequest websiteRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Website> websiteOpt = websiteRepository.findByWebsiteIdAndUserId(websiteId, userDetails.getId());
        if (websiteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Website website = websiteOpt.get();
        website.setName(websiteRequest.getName());
        website.setDomain(websiteRequest.getDomain());
        Website updated = websiteRepository.save(website);
        logger.info("Website updated: {}", websiteRequest.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{websiteId}")
    public ResponseEntity<?> deleteWebsite(@PathVariable UUID websiteId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Website> websiteOpt = websiteRepository.findByWebsiteIdAndUserId(websiteId, userDetails.getId());
        if (websiteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        websiteRepository.delete(websiteOpt.get());
        logger.info("Website deleted: {}", websiteId);
        return ResponseEntity.ok(new MessageResponse("Website deleted successfully", true));
    }
}
