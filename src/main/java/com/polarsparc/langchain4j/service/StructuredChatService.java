/*
 * Name:   StructuredChatService
 * Author: Bhaskar S
 * Date:   12/30/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;

@Service
public class StructuredChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructuredChatService.class);

    private final ChatModel chatModel;

    public StructuredChatService(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String structuredChat(String userMessage) {
        LOGGER.info("Processing chat request with -> user prompt: {}", userMessage);

        ResponseFormat format = responseFormat();

        ChatRequest chatRequest = ChatRequest.builder()
                .responseFormat(format)
                .messages(UserMessage.from(userMessage))
                .build();

        ChatResponse response = chatModel.chat(chatRequest);
        AiMessage aiMessage = response.aiMessage();

        LOGGER.info("Chat response generated successfully -> {}", aiMessage.text());

        return aiMessage.text();
    }

    private ResponseFormat responseFormat() {
        return ResponseFormat.builder()
            .type(JSON)
            .jsonSchema(JsonSchema.builder()
                .name("GpuSpecs")
                .rootElement(JsonObjectSchema.builder()
                    .addStringProperty("name")
                    .addStringProperty("bus")
                    .addIntegerProperty("memory")
                    .addIntegerProperty("clock")
                    .addIntegerProperty("cores")
                    .required("name", "bus", "memory", "clock", "cores")
                    .build())
                .build())
            .build();
    }
}
