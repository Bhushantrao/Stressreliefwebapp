package com.stressless.service;

import com.stressless.dto.request.RelaxationResourceRequest;
import com.stressless.entity.RelaxationResource;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.repository.RelaxationResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelaxationResourceService {

    @Autowired
    private RelaxationResourceRepository resourceRepository;

    public List<RelaxationResource> getAllResources(RelaxationResource.Category category) {
        if (category != null) {
            return resourceRepository.findByCategory(category);
        }
        return resourceRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<RelaxationResource> getFeaturedResources() {
        return resourceRepository.findByIsFeaturedTrue();
    }

    public RelaxationResource getById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relaxation resource not found with id: " + id));
    }

    @Transactional
    public RelaxationResource createResource(RelaxationResourceRequest request) {
        RelaxationResource resource = new RelaxationResource(
                request.getTitle().trim(),
                request.getCategory(),
                request.getAudioUrl().trim(),
                request.getIconName(),
                request.getDescription()
        );
        resource.setDurationSeconds(request.getDurationSeconds() != null ? request.getDurationSeconds() : 1800);
        resource.setFeatured(request.isFeatured());

        return resourceRepository.save(resource);
    }

    @Transactional
    public RelaxationResource updateResource(Long id, RelaxationResourceRequest request) {
        RelaxationResource resource = getById(id);

        resource.setTitle(request.getTitle().trim());
        resource.setCategory(request.getCategory());
        resource.setAudioUrl(request.getAudioUrl().trim());
        resource.setIconName(request.getIconName());
        resource.setDescription(request.getDescription());
        resource.setDurationSeconds(request.getDurationSeconds() != null ? request.getDurationSeconds() : 1800);
        resource.setFeatured(request.isFeatured());

        return resourceRepository.save(resource);
    }

    @Transactional
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Relaxation resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }
}
