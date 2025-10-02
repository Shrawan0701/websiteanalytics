package com.analytics.repository;

import com.analytics.entity.DailyAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyAggregateRepository extends JpaRepository<DailyAggregate, Long> {

    List<DailyAggregate> findByWebsiteWebsiteIdAndDateBetweenOrderByDateDesc(
            UUID websiteId, LocalDate startDate, LocalDate endDate);

    List<DailyAggregate> findByWebsiteWebsiteIdOrderByDateDesc(UUID websiteId);

    Optional<DailyAggregate> findByWebsiteWebsiteIdAndDate(UUID websiteId, LocalDate date);

    @Query("SELECT SUM(da.pageViews) FROM DailyAggregate da WHERE da.website.websiteId = :websiteId AND da.date BETWEEN :startDate AND :endDate")
    Long getTotalPageViewsByDateRange(
            @Param("websiteId") UUID websiteId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(da.clicks) FROM DailyAggregate da WHERE da.website.websiteId = :websiteId AND da.date BETWEEN :startDate AND :endDate")
    Long getTotalClicksByDateRange(
            @Param("websiteId") UUID websiteId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(da.uniqueVisitors) FROM DailyAggregate da WHERE da.website.websiteId = :websiteId AND da.date BETWEEN :startDate AND :endDate")
    Double getAverageUniqueVisitorsByDateRange(
            @Param("websiteId") UUID websiteId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(da.bounceRate) FROM DailyAggregate da WHERE da.website.websiteId = :websiteId AND da.date BETWEEN :startDate AND :endDate")
    Double getAverageBounceRateByDateRange(
            @Param("websiteId") UUID websiteId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT da FROM DailyAggregate da WHERE da.website.websiteId = :websiteId ORDER BY da.date DESC")
    List<DailyAggregate> findLatestByWebsiteWebsiteId(@Param("websiteId") UUID websiteId);

    void deleteByWebsiteWebsiteId(UUID websiteId);
}
