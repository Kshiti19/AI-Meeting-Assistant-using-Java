package com.example.aimeetingassistant.dto;

// Add Lombok imports and @Data annotation if using Lombok
// import lombok.Data;
// @Data
public class SummarizationRequest {
    private String textToSummarize;

    // Default constructor is needed for JSON deserialization
    public SummarizationRequest() {
    }

    // Optional constructor for convenience
    public SummarizationRequest(String textToSummarize) {
        this.textToSummarize = textToSummarize;
    }

    // Getter
    public String getTextToSummarize() {
        return textToSummarize;
    }

    // Setter
    public void setTextToSummarize(String textToSummarize) {
        this.textToSummarize = textToSummarize;
    }
}