/*
 * Name:   SimpleChatController
 * Author: Bhaskar S
 * Date:   12/30/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.controller;

import com.polarsparc.langchain4j.service.SimpleChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SimpleChatController {
    private final SimpleChatService simpleChatService;

    public SimpleChatController(SimpleChatService simpleChatService) {
        this.simpleChatService = simpleChatService;
    }

    @PostMapping("/simplechat")
    public ResponseEntity<String> simpleChat(@RequestBody String message) {
        String response = simpleChatService.simpleChat(message);
        return ResponseEntity.ok(response);
    }
}
