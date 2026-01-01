/*
 * Name:   StructuredChatController
 * Author: Bhaskar S
 * Date:   12/30/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.controller;

import com.polarsparc.langchain4j.service.StructuredChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class StructuredChatController {
    private final StructuredChatService structuredChatService;

    public StructuredChatController(StructuredChatService structuredChatService) {
        this.structuredChatService = structuredChatService;
    }

    @PostMapping("/structchat")
    public ResponseEntity<String> structuredChat(@RequestBody String message) {
        String response = structuredChatService.structuredChat(message);
        return ResponseEntity.ok(response);
    }
}
