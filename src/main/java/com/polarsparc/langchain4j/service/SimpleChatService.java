/*
 * Name:   SimpleChatService
 * Author: Bhaskar S
 * Date:   12/30/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleChatService.class);

    private final ChatModel chatModel;

    public SimpleChatService(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String simpleChat(String userMessage) {
        LOGGER.info("Processing chat request with -> user prompt: {}", userMessage);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.from("You are a helpful assistant."));
        messages.add(UserMessage.from(userMessage));

        ChatResponse response = chatModel.chat(messages);
        AiMessage aiMessage = response.aiMessage();

        LOGGER.info("Chat response generated successfully -> {}", aiMessage.text());

        return aiMessage.text();
    }
}
