package com.analytics.service;

import com.analytics.dto.EventRequest;
import com.analytics.entity.Event;
import com.analytics.entity.Website;
import com.analytics.repository.EventRepository;
import com.analytics.repository.WebsiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private WebsiteRepository websiteRepository;

    public void trackEvent(EventRequest eventRequest, String ipAddress) {
        try {
            UUID websiteId = UUID.fromString(eventRequest.getWebsiteId());

            // Verify website exists
            Website website = websiteRepository.findByWebsiteId(websiteId)
                    .orElseThrow(() -> new IllegalArgumentException("Website not found: " + websiteId));

            Event event = new Event();
            event.setWebsite(website);  // Set Website entity, not websiteId UUID
            event.setEventType(eventRequest.getEventType());
            event.setPageUrl(eventRequest.getPageUrl());
            event.setEventName(eventRequest.getEventName());
            event.setProperties(eventRequest.getProperties());
            event.setUserAgent(eventRequest.getUserAgent());
            event.setIpAddress(ipAddress);
            event.setSessionId(eventRequest.getSessionId());

            eventRepository.save(event);

            logger.debug("Event tracked: {} for website {}", eventRequest.getEventType(), websiteId);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid event data: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to track event: {}", e.getMessage());
            throw new RuntimeException("Failed to track event: " + e.getMessage());
        }
    }


    public void trackEvents(EventRequest[] eventRequests, String ipAddress) {
        for (EventRequest eventRequest : eventRequests) {
            try {
                trackEvent(eventRequest, ipAddress);
            } catch (Exception e) {
                logger.warn("Failed to track batch event: {}", e.getMessage());
            }
        }
    }
}
