package com.analytics.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_aggregates",
        uniqueConstraints = @UniqueConstraint(columnNames = {"website_id", "date"}))
public class DailyAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id", referencedColumnName = "website_id", nullable = false)
    @JsonIgnore
    private Website website;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "page_views")
    private Integer pageViews = 0;

    @Column(name = "clicks")
    private Integer clicks = 0;

    @Column(name = "unique_visitors")
    private Integer uniqueVisitors = 0;

    @Column(name = "bounce_rate", precision = 5, scale = 2)
    private BigDecimal bounceRate = BigDecimal.ZERO;

    @Column(name = "avg_session_duration")
    private Integer avgSessionDuration = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public DailyAggregate() {}

    public DailyAggregate(Website website, LocalDate date) {
        this.website = website;
        this.date = date;
    }

    // Getters and Setters below

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Website getWebsite() { return website; }
    public void setWebsite(Website website) { this.website = website; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getPageViews() { return pageViews; }
    public void setPageViews(Integer pageViews) { this.pageViews = pageViews; }

    public Integer getClicks() { return clicks; }
    public void setClicks(Integer clicks) { this.clicks = clicks; }

    public Integer getUniqueVisitors() { return uniqueVisitors; }
    public void setUniqueVisitors(Integer uniqueVisitors) { this.uniqueVisitors = uniqueVisitors; }

    public BigDecimal getBounceRate() { return bounceRate; }
    public void setBounceRate(BigDecimal bounceRate) { this.bounceRate = bounceRate; }

    public Integer getAvgSessionDuration() { return avgSessionDuration; }
    public void setAvgSessionDuration(Integer avgSessionDuration) { this.avgSessionDuration = avgSessionDuration; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "DailyAggregate{" +
                "id=" + id +
                ", website=" + website +
                ", date=" + date +
                ", pageViews=" + pageViews +
                ", clicks=" + clicks +
                ", uniqueVisitors=" + uniqueVisitors +
                '}';
    }
}
