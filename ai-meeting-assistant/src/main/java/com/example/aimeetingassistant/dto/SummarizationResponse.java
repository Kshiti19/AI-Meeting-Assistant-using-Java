package com.example.aimeetingassistant.dto;

// Add Lombok imports and @Data annotation if using Lombok
// import lombok.Data;
// @Data
public class SummarizationResponse {
    private String summary;

    // Default constructor
    public SummarizationResponse() {
    }

    // Constructor for convenience
    public SummarizationResponse(String summary) {
        this.summary = summary;
    }

    // Getter
    public String getSummary() {
        return summary;
    }

    // Setter
    public void setSummary(String summary) {
        this.summary = summary;
    }
}