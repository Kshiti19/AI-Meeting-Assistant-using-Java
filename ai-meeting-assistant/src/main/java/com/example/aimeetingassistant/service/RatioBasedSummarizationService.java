package com.example.aimeetingassistant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary; // Import @Primary
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A mock summarization service that truncates the text to roughly 1/3rd
 * of its original word count by taking the beginning portion.
 */
@Service // Mark as a Spring service component
@Primary // <<< Add this annotation
public class RatioBasedSummarizationService implements SummarizationService {

    private static final Logger log = LoggerFactory.getLogger(RatioBasedSummarizationService.class);

    // Set a minimum number of words required before attempting ratio-based summary
    private static final int MIN_WORDS_FOR_RATIO_SUMMARY = 100;
    private static final String SUMMARY_PREFIX = "[Ratio Summary]\n";

    @Override
    public String summarize(String text) throws Exception { // Match the interface signature
        if (text == null || text.isBlank()) {
            log.warn("Input text for summarization is null or blank.");
            // Return a consistent error message format
            return SUMMARY_PREFIX.trim() + " Error: Cannot summarize empty text.";
        }

        // Trim leading/trailing whitespace before splitting
        String trimmedText = text.trim();

        // Split into words using whitespace (includes spaces, tabs, newlines)
        String[] words = trimmedText.split("\\s+");
        int totalWords = words.length;
        log.debug("Original word count: {}", totalWords);

        // If the text is too short, just return the original (with prefix)
        if (totalWords < MIN_WORDS_FOR_RATIO_SUMMARY) {
            log.debug("Text too short ({} words), returning original cleaned text.", totalWords);
            return SUMMARY_PREFIX + trimmedText;
        }

        // Calculate the target number of words (integer division is okay for
        // approximation)
        int targetWordCount = totalWords / 3;

        // Ensure we take at least a few words if the division result is very small
        targetWordCount = Math.max(targetWordCount, 5); // e.g., ensure minimum 5 words summary

        // Make sure target count doesn't exceed total words (shouldn't with division,
        // but safe)
        targetWordCount = Math.min(targetWordCount, totalWords);

        log.debug("Target summary word count: ~{}", targetWordCount);

        // Build the summary using the first 'targetWordCount' words
        // Using Streams API for a concise way to join words
        String summaryContent = Arrays.stream(words)
                .limit(targetWordCount) // Take the first 'targetWordCount' words
                .collect(Collectors.joining(" ")); // Join them back with single spaces

        // Add ellipsis "..." to indicate truncation if we didn't use the whole text
        String ellipsis = (targetWordCount < totalWords) ? " ..." : "";

        String finalSummary = SUMMARY_PREFIX + summaryContent + ellipsis;
        log.debug("Generated ratio-based summary.");
        return finalSummary;
    }
}