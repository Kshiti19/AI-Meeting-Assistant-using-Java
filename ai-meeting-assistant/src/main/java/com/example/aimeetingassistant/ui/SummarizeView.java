package com.example.aimeetingassistant.ui;

import com.example.aimeetingassistant.service.SummarizationService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier; // <-- Import Added
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger; // Import Logger
import org.slf4j.LoggerFactory; // Import LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class SummarizeView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(SummarizeView.class); // Add logger if needed

    private final SummarizationService summarizationService;

    private final TextArea meetingTextArea;
    private final TextArea summaryOutputArea;
    private final Button summarizeButton;
    private final ProgressBar progressBar;

    @Autowired
    public SummarizeView(SummarizationService summarizationService) {
        this.summarizationService = summarizationService;

        // --- Create UI Components ---
        H1 title = new H1("AI Meeting Summarizer (Vaadin UI)");

        meetingTextArea = new TextArea("Paste Meeting Transcript:");
        meetingTextArea.setWidthFull();
        meetingTextArea.setPlaceholder("Enter the full meeting text or notes here...");
        meetingTextArea.setMinHeight("200px");

        summaryOutputArea = new TextArea("Summary:");
        summaryOutputArea.setWidthFull();
        summaryOutputArea.setReadOnly(true);
        summaryOutputArea.setMinHeight("150px");
        summaryOutputArea.setPlaceholder("The generated summary will appear here...");

        summarizeButton = new Button("Summarize Text");
        summarizeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        summarizeButton.setWidthFull();

        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setWidthFull();

        // --- Configure Listeners ---
        summarizeButton.addClickListener(event -> handleSummarization());

        // Corrected KeyDown listener
        meetingTextArea.addKeyDownListener(Key.ENTER, event -> {
            // Check if Shift key modifier IS NOT present
            if (!event.getModifiers().contains(KeyModifier.SHIFT)) { // <-- Corrected line
                log.debug("Enter pressed without Shift, handling summarization.");
                handleSummarization();
            } else {
                log.debug("Enter pressed with Shift, allowing newline.");
                // Allow default behavior (newline) if Shift is pressed
            }
        });

        // --- Arrange Components ---
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        setMaxWidth("850px");
        getStyle().set("margin", "0 auto");

        add(
                title,
                meetingTextArea,
                summarizeButton,
                progressBar,
                summaryOutputArea);
    }

    private void handleSummarization() {
        String inputText = meetingTextArea.getValue();
        log.info("Summarization requested."); // Add logging

        if (inputText == null || inputText.trim().isEmpty()) {
            log.warn("Summarization attempt with empty input.");
            showNotification("Please enter some text to summarize.", NotificationVariant.LUMO_WARNING);
            return;
        }

        setProcessingState(true); // Disable button, show progress

        try {
            log.debug("Calling summarization service...");
            String summaryResult = summarizationService.summarize(inputText);
            log.info("Summarization successful.");
            summaryOutputArea.setValue(summaryResult);
        } catch (Exception e) {
            log.error("Error during summarization call", e);
            showNotification("Error during summarization: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
            summaryOutputArea.setValue("Summarization failed.");
        } finally {
            log.debug("Resetting UI processing state.");
            setProcessingState(false); // Re-enable button, hide progress
        }
    }

    // Helper to set UI state during processing
    private void setProcessingState(boolean processing) {
        summarizeButton.setEnabled(!processing);
        progressBar.setVisible(processing);
        // Optionally make input read-only during processing
        // meetingTextArea.setReadOnly(processing);
    }

    // Helper for notifications
    private void showNotification(String message, NotificationVariant variant) {
        Notification.show(message, 3000, Notification.Position.MIDDLE)
                .addThemeVariants(variant);
    }
}