package com.stressless.controller;

import com.stressless.dto.response.ApiResponse;
import com.stressless.entity.RelaxationResource;
import com.stressless.entity.RelaxationResource.Category;
import com.stressless.service.RelaxationResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private RelaxationResourceService resourceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RelaxationResource>>> getResources(
            @RequestParam(required = false) Category category) {
        List<RelaxationResource> resources = resourceService.getAllResources(category);
        return ResponseEntity.ok(ApiResponse.ok("Relaxation resources retrieved", resources));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<RelaxationResource>>> getFeaturedResources() {
        List<RelaxationResource> resources = resourceService.getFeaturedResources();
        return ResponseEntity.ok(ApiResponse.ok("Featured relaxation sounds retrieved", resources));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RelaxationResource>> getResourceById(@PathVariable Long id) {
        RelaxationResource resource = resourceService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Resource details retrieved", resource));
    }
}
