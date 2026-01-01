/*
 * Name:   ChatHistoryController
 * Author: Bhaskar S
 * Date:   12/31/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.controller;

import com.polarsparc.langchain4j.service.ChatHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ChatHistoryController {
    private final ChatHistoryService chatHistoryService;

    public ChatHistoryController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    @PostMapping("/chathistory")
    public ResponseEntity<String> simpleChat(@RequestBody String message) {
        String response = chatHistoryService.chatWithHistory(message);
        return ResponseEntity.ok(response);
    }
}
