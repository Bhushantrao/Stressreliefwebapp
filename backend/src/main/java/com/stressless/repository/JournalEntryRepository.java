package com.stressless.repository;

import com.stressless.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<JournalEntry> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT j FROM JournalEntry j WHERE j.user.id = :userId AND " +
           "(LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(j.content) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(j.tags) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY j.createdAt DESC")
    List<JournalEntry> searchUserJournals(@Param("userId") Long userId, @Param("query") String query);

    long countByUserId(Long userId);
}
