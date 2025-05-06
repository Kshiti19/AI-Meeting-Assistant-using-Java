package com.example.aimeetingassistant.controller;

import com.example.aimeetingassistant.dto.SummarizationRequest;
import com.example.aimeetingassistant.dto.SummarizationResponse;
import com.example.aimeetingassistant.service.SummarizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private final SummarizationService summarizationService;

    @Autowired
    public ApiController(SummarizationService summarizationService) {
        this.summarizationService = summarizationService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<SummarizationResponse> summarizeText(@RequestBody SummarizationRequest request) {
        log.info("Received summarization request: {}", request);

        if (request == null || request.getTextToSummarize() == null || request.getTextToSummarize().trim().isEmpty()) {
            log.warn("Received empty or null text for summarization.");
            return ResponseEntity.badRequest()
                    .body(new SummarizationResponse("Error: Input text cannot be empty or null."));
        }

        try {
            String summaryResult = summarizationService.summarize(request.getTextToSummarize().trim());
            return ResponseEntity.ok(new SummarizationResponse(summaryResult));
        } catch (InterruptedException e) {
            log.error("Summarization interrupted", e);
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SummarizationResponse("Summarization process was interrupted. Please try again."));
        } catch (Exception e) {
            log.error("Error during summarization: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SummarizationResponse("Internal error during summarization."));
        }
    }
}
