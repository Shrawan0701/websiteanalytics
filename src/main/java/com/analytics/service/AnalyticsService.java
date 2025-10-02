package com.analytics.service;

import com.analytics.dto.AnalyticsResponse;
import com.analytics.entity.DailyAggregate;
import com.analytics.entity.Event;
import com.analytics.repository.DailyAggregateRepository;
import com.analytics.repository.EventRepository;
import com.analytics.repository.WebsiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DailyAggregateRepository dailyAggregateRepository;

    @Autowired
    private WebsiteRepository websiteRepository;

    public AnalyticsResponse getAnalyticsOverview(UUID websiteId, LocalDate startDate, LocalDate endDate, Long userId) {
        websiteRepository.findByUserIdAndWebsiteId(userId, websiteId)
                .orElseThrow(() -> new IllegalArgumentException("Website not found or access denied"));

        AnalyticsResponse response = new AnalyticsResponse();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        Long pageViews = eventRepository.countByWebsiteWebsiteIdAndEventTypeAndCreatedAtBetween(
                websiteId, "page_view", startDateTime, endDateTime);

        Long clicks = eventRepository.countByWebsiteWebsiteIdAndEventTypeAndCreatedAtBetween(
                websiteId, "click", startDateTime, endDateTime);

        Long uniqueVisitors = eventRepository.countUniqueVisitors(websiteId, startDateTime, endDateTime);

        Long totalEvents = (pageViews != null ? pageViews : 0L) + (clicks != null ? clicks : 0L);

        response.setTotalPageViews(pageViews != null ? pageViews : 0L);
        response.setTotalClicks(clicks != null ? clicks : 0L);
        response.setTotalEvents(totalEvents);
        response.setUniqueVisitors(uniqueVisitors != null ? uniqueVisitors : 0L);

        Double avgBounceRate = dailyAggregateRepository.getAverageBounceRateByDateRange(websiteId, startDate, endDate);
        response.setBounceRate(avgBounceRate != null ? BigDecimal.valueOf(avgBounceRate) : BigDecimal.valueOf(0.0));

        response.setAvgSessionDuration(180);

        List<DailyAggregate> dailyAggregates = dailyAggregateRepository
                .findByWebsiteWebsiteIdAndDateBetweenOrderByDateDesc(websiteId, startDate, endDate);

        List<AnalyticsResponse.DailyStats> dailyStatsList = dailyAggregates.stream()
                .map(da -> new AnalyticsResponse.DailyStats(
                        da.getDate(),
                        Long.valueOf(da.getPageViews()),
                        Long.valueOf(da.getClicks()),
                        Long.valueOf(da.getUniqueVisitors())
                ))
                .collect(Collectors.toList());

        response.setDailyStats(dailyStatsList);

        List<Object[]> eventTypeData = eventRepository.getEventTypeDistribution(websiteId, startDateTime, endDateTime);
        Map<String, Long> eventTypeDistribution = eventTypeData.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
        response.setEventTypeDistribution(eventTypeDistribution);

        logger.info("Generated analytics overview for website {} ({} to {})", websiteId, startDate, endDate);
        return response;
    }

    public Page<Event> getEvents(UUID websiteId, int page, int size, String eventType,
                                 LocalDate startDate, LocalDate endDate, Long userId) {
        websiteRepository.findByUserIdAndWebsiteId(userId, websiteId)
                .orElseThrow(() -> new IllegalArgumentException("Website not found or access denied"));

        Pageable pageable = PageRequest.of(page, size);

        if (eventType != null && !eventType.isEmpty()) {
            return eventRepository.findByWebsiteWebsiteIdAndEventTypeOrderByCreatedAtDesc(websiteId, eventType, pageable);
        } else {
            return eventRepository.findByWebsiteWebsiteIdOrderByCreatedAtDesc(websiteId, pageable);
        }
    }

    public List<Map<String, Object>> getChartData(UUID websiteId, LocalDate startDate, LocalDate endDate,
                                                  String eventType, Long userId) {
        websiteRepository.findByUserIdAndWebsiteId(userId, websiteId)
                .orElseThrow(() -> new IllegalArgumentException("Website not found or access denied"));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<Object[]> rawData = eventRepository.getDailyEventCounts(websiteId, eventType, startDateTime, endDateTime);

        return rawData.stream()
                .map(row -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("date", row[0].toString());
                    dataPoint.put("count", ((Number) row[1]).longValue());
                    return dataPoint;
                })
                .collect(Collectors.toList());
    }

    public List<AnalyticsResponse.PageStats> getTopPages(UUID websiteId, LocalDate startDate, LocalDate endDate,
                                                         int limit, Long userId) {
        websiteRepository.findByUserIdAndWebsiteId(userId, websiteId)
                .orElseThrow(() -> new IllegalArgumentException("Website not found or access denied"));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> rawData = eventRepository.getTopPages(websiteId, startDateTime, endDateTime, pageable);

        return rawData.stream()
                .map(row -> new AnalyticsResponse.PageStats(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getSummary(UUID websiteId, Long userId) {
        websiteRepository.findByUserIdAndWebsiteId(userId, websiteId)
                .orElseThrow(() -> new IllegalArgumentException("Website not found or access denied"));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        Map<String, Object> summary = new HashMap<>();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        Long totalEvents = eventRepository.countByWebsiteWebsiteIdAndEventTypeAndCreatedAtBetween(
                websiteId, "page_view", startDateTime, endDateTime) +
                eventRepository.countByWebsiteWebsiteIdAndEventTypeAndCreatedAtBetween(
                        websiteId, "click", startDateTime, endDateTime);

        Long uniqueVisitors = eventRepository.countUniqueVisitors(websiteId, startDateTime, endDateTime);

        summary.put("totalEvents", totalEvents != null ? totalEvents : 0L);
        summary.put("uniqueVisitors", uniqueVisitors != null ? uniqueVisitors : 0L);
        summary.put("dateRange", Map.of("start", startDate, "end", endDate));

        return summary;
    }
}
