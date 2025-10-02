package com.analytics.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id", referencedColumnName = "website_id", nullable = false)
    @JsonIgnore
    private Website website;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "page_url", columnDefinition = "TEXT")
    private String pageUrl;

    @Column(name = "event_name")
    private String eventName;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> properties;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Event() {}

    public Event(Website website, String eventType, String pageUrl) {
        this.website = website;
        this.eventType = eventType;
        this.pageUrl = pageUrl;
    }

    // Getters and setters without websiteId field

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Website getWebsite() { return website; }
    public void setWebsite(Website website) { this.website = website; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPageUrl() { return pageUrl; }
    public void setPageUrl(String pageUrl) { this.pageUrl = pageUrl; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", website=" + website +
                ", eventType='" + eventType + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
