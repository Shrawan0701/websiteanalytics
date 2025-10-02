package com.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AnalyticsResponse {
    private Long totalPageViews;
    private Long totalClicks;
    private Long totalEvents;
    private Long uniqueVisitors;
    private BigDecimal bounceRate;
    private Integer avgSessionDuration;
    private List<DailyStats> dailyStats;
    private List<Map<String, Object>> chartData;
    private List<PageStats> topPages;
    private Map<String, Long> eventTypeDistribution;

    public AnalyticsResponse() {}

    // Getters and Setters
    public Long getTotalPageViews() { return totalPageViews; }
    public void setTotalPageViews(Long totalPageViews) { this.totalPageViews = totalPageViews; }

    public Long getTotalClicks() { return totalClicks; }
    public void setTotalClicks(Long totalClicks) { this.totalClicks = totalClicks; }

    public Long getTotalEvents() { return totalEvents; }
    public void setTotalEvents(Long totalEvents) { this.totalEvents = totalEvents; }

    public Long getUniqueVisitors() { return uniqueVisitors; }
    public void setUniqueVisitors(Long uniqueVisitors) { this.uniqueVisitors = uniqueVisitors; }

    public BigDecimal getBounceRate() { return bounceRate; }
    public void setBounceRate(BigDecimal bounceRate) { this.bounceRate = bounceRate; }

    public Integer getAvgSessionDuration() { return avgSessionDuration; }
    public void setAvgSessionDuration(Integer avgSessionDuration) { this.avgSessionDuration = avgSessionDuration; }

    public List<DailyStats> getDailyStats() { return dailyStats; }
    public void setDailyStats(List<DailyStats> dailyStats) { this.dailyStats = dailyStats; }

    public List<Map<String, Object>> getChartData() { return chartData; }
    public void setChartData(List<Map<String, Object>> chartData) { this.chartData = chartData; }

    public List<PageStats> getTopPages() { return topPages; }
    public void setTopPages(List<PageStats> topPages) { this.topPages = topPages; }

    public Map<String, Long> getEventTypeDistribution() { return eventTypeDistribution; }
    public void setEventTypeDistribution(Map<String, Long> eventTypeDistribution) { this.eventTypeDistribution = eventTypeDistribution; }

    // Inner classes
    public static class DailyStats {
        private LocalDate date;
        private Long pageViews;
        private Long clicks;
        private Long uniqueVisitors;

        public DailyStats() {}

        public DailyStats(LocalDate date, Long pageViews, Long clicks, Long uniqueVisitors) {
            this.date = date;
            this.pageViews = pageViews;
            this.clicks = clicks;
            this.uniqueVisitors = uniqueVisitors;
        }

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public Long getPageViews() { return pageViews; }
        public void setPageViews(Long pageViews) { this.pageViews = pageViews; }

        public Long getClicks() { return clicks; }
        public void setClicks(Long clicks) { this.clicks = clicks; }

        public Long getUniqueVisitors() { return uniqueVisitors; }
        public void setUniqueVisitors(Long uniqueVisitors) { this.uniqueVisitors = uniqueVisitors; }
    }

    public static class PageStats {
        private String pageUrl;
        private Long visits;

        public PageStats() {}

        public PageStats(String pageUrl, Long visits) {
            this.pageUrl = pageUrl;
            this.visits = visits;
        }

        public String getPageUrl() { return pageUrl; }
        public void setPageUrl(String pageUrl) { this.pageUrl = pageUrl; }

        public Long getVisits() { return visits; }
        public void setVisits(Long visits) { this.visits = visits; }
    }
}
