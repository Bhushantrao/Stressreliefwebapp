package com.stressless.dto.request;

import jakarta.validation.constraints.NotBlank;

public class QuoteRequest {

    @NotBlank(message = "Quote text is required")
    private String quoteText;

    private String author = "Unknown";
    private String category = "Mindfulness";
    private boolean active = true;

    public QuoteRequest() {
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
