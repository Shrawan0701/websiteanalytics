package com.analytics.controller;

import com.analytics.dto.AnalyticsResponse;
import com.analytics.entity.Event;
import com.analytics.service.AnalyticsService;
import com.analytics.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{websiteId}/overview")
    public ResponseEntity<AnalyticsResponse> getOverview(@PathVariable UUID websiteId,
                                                         @RequestParam(defaultValue = "30") int days, Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days);
        AnalyticsResponse response = analyticsService.getAnalyticsOverview(websiteId, start, end, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{websiteId}/events")
    public ResponseEntity<Page<Event>> getEvents(
            @PathVariable UUID websiteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Page<Event> events = analyticsService.getEvents(websiteId, page, size, eventType, startDate, endDate, user.getId());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{websiteId}/chart-data")
    public ResponseEntity<List<Map<String, Object>>> getChartData(@PathVariable UUID websiteId,
                                                                  @RequestParam(defaultValue = "30") int days,
                                                                  @RequestParam(defaultValue = "page_view") String eventType,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                  Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        LocalDate start = startDate != null ? startDate : end.minusDays(days);
        List<Map<String, Object>> data = analyticsService.getChartData(websiteId, start, end, eventType, user.getId());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{websiteId}/top-pages")
    public ResponseEntity<List<AnalyticsResponse.PageStats>> getTopPages(@PathVariable UUID websiteId,
                                                                         @RequestParam(defaultValue = "10") int limit, Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        List<AnalyticsResponse.PageStats> pages = analyticsService.getTopPages(websiteId, LocalDate.now().minusDays(30), LocalDate.now(), limit, user.getId());
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{websiteId}/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@PathVariable UUID websiteId, Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> summary = analyticsService.getSummary(websiteId, user.getId());
        return ResponseEntity.ok(summary);
    }
}
