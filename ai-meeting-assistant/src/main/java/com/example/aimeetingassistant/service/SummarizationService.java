package com.example.aimeetingassistant.service;

public interface SummarizationService {
    /**
     * Generates a summary for the given text.
     * 
     * @param text The input text (e.g., meeting transcript).
     * @return The generated summary.
     * @throws Exception If any error occurs during summarization.
     */
    String summarize(String text) throws Exception;
}