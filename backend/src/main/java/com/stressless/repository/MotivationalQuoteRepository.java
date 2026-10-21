package com.stressless.repository;

import com.stressless.entity.MotivationalQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotivationalQuoteRepository extends JpaRepository<MotivationalQuote, Long> {
    List<MotivationalQuote> findByActiveTrue();

    @Query(value = "SELECT * FROM motivational_quotes WHERE active = true ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<MotivationalQuote> findRandomActiveQuote();
}
