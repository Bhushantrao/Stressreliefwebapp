package com.stressless.service;

import com.stressless.dto.request.QuoteRequest;
import com.stressless.entity.MotivationalQuote;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.repository.MotivationalQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MotivationalQuoteService {

    @Autowired
    private MotivationalQuoteRepository quoteRepository;

    public MotivationalQuote getDailyQuote() {
        return quoteRepository.findRandomActiveQuote()
                .orElseGet(() -> {
                    MotivationalQuote defaultQuote = new MotivationalQuote(
                            "Feelings come and go like clouds in a windy sky. Conscious breathing is my anchor.",
                            "Thich Nhat Hanh",
                            "Mindfulness"
                    );
                    return quoteRepository.save(defaultQuote);
                });
    }

    public List<MotivationalQuote> getAllActiveQuotes() {
        return quoteRepository.findByActiveTrue();
    }

    public List<MotivationalQuote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Transactional
    public MotivationalQuote createQuote(QuoteRequest request) {
        MotivationalQuote quote = new MotivationalQuote(
                request.getQuoteText().trim(),
                request.getAuthor() != null ? request.getAuthor().trim() : "Unknown",
                request.getCategory() != null ? request.getCategory().trim() : "Mindfulness"
        );
        quote.setActive(request.isActive());
        return quoteRepository.save(quote);
    }

    @Transactional
    public MotivationalQuote updateQuote(Long id, QuoteRequest request) {
        MotivationalQuote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + id));

        quote.setQuoteText(request.getQuoteText().trim());
        if (request.getAuthor() != null) quote.setAuthor(request.getAuthor().trim());
        if (request.getCategory() != null) quote.setCategory(request.getCategory().trim());
        quote.setActive(request.isActive());

        return quoteRepository.save(quote);
    }

    @Transactional
    public void deleteQuote(Long id) {
        if (!quoteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quote not found with id: " + id);
        }
        quoteRepository.deleteById(id);
    }
}
