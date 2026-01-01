/*
 * Name:   ToolsAgentController
 * Author: Bhaskar S
 * Date:   12/31/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.controller;

import com.polarsparc.langchain4j.service.ToolsAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ToolsAgentController {
    private final ToolsAgentService toolsAgentService;

    public ToolsAgentController(ToolsAgentService toolsAgentService) {
        this.toolsAgentService = toolsAgentService;
    }

    @PostMapping("/agentchat")
    public ResponseEntity<String> agentChat(@RequestBody String message) {
        String response = toolsAgentService.agentChat(message);
        return ResponseEntity.ok(response);
    }
}
