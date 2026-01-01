/*
 * Name:   ChatHistoryService
 * Author: Bhaskar S
 * Date:   12/31/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatHistoryService.class);

    private final ChatModel chatModel;
    private final ChatAssistant chatAssistant;

    public ChatHistoryService(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
        this.chatAssistant = setUpChatAssistantWithMemory();
    }

    public String chatWithHistory(String userMessage) {
        LOGGER.info("Processing chat request with -> user prompt: {}", userMessage);

        ChatResponse response = chatAssistant.chat(userMessage);
        AiMessage aiMessage = response.aiMessage();

        LOGGER.info("Chat response generated successfully -> {}", aiMessage.text());

        return aiMessage.text();
    }

    private ChatAssistant setUpChatAssistantWithMemory() {
        ChatMemoryStore chatMemoryStore = new InMemoryChatMemoryStore();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
            .id("uid-1")
            .maxMessages(10)
            .chatMemoryStore(chatMemoryStore)
            .build();

        return AiServices.builder(ChatAssistant.class)
            .chatModel(chatModel)
            .chatMemory(chatMemory)
            .build();
    }

    interface ChatAssistant {
        @SystemMessage("You are a helpful assistant.")
        ChatResponse chat(@UserMessage String userMessage);
    }
}
