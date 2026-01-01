/*
 * Name:   VectorEmbeddingController
 * Author: Bhaskar S
 * Date:   12/30/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.controller;

import com.polarsparc.langchain4j.model.Book;
import com.polarsparc.langchain4j.service.VectorSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VectorSearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VectorSearchController.class);

    private final ObjectMapper objectMapper;
    private final VectorSearchService vectorSearchService;

    public VectorSearchController(VectorSearchService vectorSearchService) {
        this.objectMapper = new ObjectMapper();
        this.vectorSearchService = vectorSearchService;
    }

    @PostMapping("/vectorsearch")
    public ResponseEntity<String> similaritySearch(@RequestBody String message) {
        List<Book> documents = vectorSearchService.similaritySearch(message, 2);

        String json = "[]";
        try {
            json = objectMapper.writeValueAsString(documents);
        } catch (Exception ex) {
            LOGGER.error("Failed to serialize documents", ex);
        }

        return ResponseEntity.ok(json);
    }
}
