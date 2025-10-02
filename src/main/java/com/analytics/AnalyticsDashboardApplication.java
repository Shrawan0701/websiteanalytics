package com.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class AnalyticsDashboardApplication {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsDashboardApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Analytics Dashboard Application...");
        SpringApplication.run(AnalyticsDashboardApplication.class, args);
        logger.info("Analytics Dashboard Application started successfully!");
    }
}
