package com.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WebsiteRequest {
    @NotBlank(message = "Website name is required")
    @Size(min = 1, max = 255, message = "Website name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Domain is required")
    @Size(max = 255, message = "Domain must not exceed 255 characters")
    @Pattern(regexp = "^https?://.*", message = "Domain must start with http:// or https://")
    private String domain;

    public WebsiteRequest() {}

    public WebsiteRequest(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
