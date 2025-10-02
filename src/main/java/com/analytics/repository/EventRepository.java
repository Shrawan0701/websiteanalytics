package com.analytics.repository;

import com.analytics.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByWebsiteWebsiteIdOrderByCreatedAtDesc(UUID websiteId, Pageable pageable);

    Page<Event> findByWebsiteWebsiteIdAndEventTypeOrderByCreatedAtDesc(UUID websiteId, String eventType, Pageable pageable);

    List<Event> findByWebsiteWebsiteIdAndCreatedAtBetweenOrderByCreatedAtDesc(UUID websiteId, LocalDateTime startDate, LocalDateTime endDate);

    List<Event> findByWebsiteWebsiteIdAndEventTypeAndCreatedAtBetween(UUID websiteId, String eventType, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.website.websiteId = :websiteId AND e.eventType = :eventType AND e.createdAt BETWEEN :startDate AND :endDate")
    Long countByWebsiteWebsiteIdAndEventTypeAndCreatedAtBetween(
            @Param("websiteId") UUID websiteId,
            @Param("eventType") String eventType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE(e.createdAt) as date, COUNT(e) as count FROM Event e WHERE e.website.websiteId = :websiteId AND e.eventType = :eventType AND e.createdAt BETWEEN :startDate AND :endDate GROUP BY DATE(e.createdAt) ORDER BY DATE(e.createdAt)")
    List<Object[]> getDailyEventCounts(
            @Param("websiteId") UUID websiteId,
            @Param("eventType") String eventType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT e.sessionId) FROM Event e WHERE e.website.websiteId = :websiteId AND e.createdAt BETWEEN :startDate AND :endDate AND e.sessionId IS NOT NULL")
    Long countUniqueVisitors(
            @Param("websiteId") UUID websiteId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e.eventType, COUNT(e) FROM Event e WHERE e.website.websiteId = :websiteId AND e.createdAt BETWEEN :startDate AND :endDate GROUP BY e.eventType")
    List<Object[]> getEventTypeDistribution(
            @Param("websiteId") UUID websiteId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e.pageUrl, COUNT(e) as visits FROM Event e WHERE e.website.websiteId = :websiteId AND e.eventType = 'page_view' AND e.createdAt BETWEEN :startDate AND :endDate GROUP BY e.pageUrl ORDER BY visits DESC")
    List<Object[]> getTopPages(
            @Param("websiteId") UUID websiteId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    void deleteByWebsiteWebsiteId(UUID websiteId);
}
