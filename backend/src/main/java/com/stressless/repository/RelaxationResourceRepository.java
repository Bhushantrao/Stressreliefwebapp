package com.stressless.repository;

import com.stressless.entity.RelaxationResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelaxationResourceRepository extends JpaRepository<RelaxationResource, Long> {
    List<RelaxationResource> findByCategory(RelaxationResource.Category category);
    List<RelaxationResource> findByIsFeaturedTrue();
    List<RelaxationResource> findAllByOrderByCreatedAtDesc();
}
