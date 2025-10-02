package com.analytics.config;

import com.analytics.security.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class WebSecurityConfig {

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow all OPTIONS
                        .requestMatchers("/auth/**", "/track/**", "/tracking.js").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/websites/**").authenticated()
                        .requestMatchers("/websites/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===== CORS configuration =====
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Existing config for general paths
        CorsConfiguration generalConfig = new CorsConfiguration();
        generalConfig.setAllowedOrigins(List.of("https://websiteanalytics.vercel.app")); // Keep your frontend
        generalConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        generalConfig.setAllowedHeaders(List.of("*"));
        generalConfig.setAllowCredentials(true);


        CorsConfiguration trackingConfig = new CorsConfiguration();
        trackingConfig.setAllowedOrigins(List.of("*")); // Allows any embedding site
        trackingConfig.setAllowedMethods(List.of("POST", "OPTIONS")); // Only what's needed for tracking
        trackingConfig.setAllowedHeaders(List.of("Content-Type")); // Minimal headers
        trackingConfig.setAllowCredentials(false); // No credentials for public tracking
        trackingConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", generalConfig); // General paths
        source.registerCorsConfiguration("/track/**", trackingConfig); // Tracking-specific
        return source;
    }

}
