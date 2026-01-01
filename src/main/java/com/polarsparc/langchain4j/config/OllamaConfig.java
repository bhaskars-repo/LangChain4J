/*
 * Name:   OllamaConfig
 * Author: Bhaskar S
 * Date:   12/30/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {
    @Value("${ollama.baseURL}")
    private String baseUrl;

    @Value("${ollama.chatModel}")
    private String chatModel;

    @Value("${ollama.toolsModel}")
    private String toolsModel;

    @Value("${ollama.temperature}")
    private double temperature;

    @Bean(name="ollamaChatModel")
    public ChatModel ollamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(chatModel)
                .temperature(temperature)
                .build();
    }

    @Bean(name="ollamaEmbeddingModel")
    public EmbeddingModel ollamaEmbeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl(baseUrl)
                .modelName(chatModel)
                .build();
    }

    @Bean(name="ollamaToolsModel")
    public ChatModel ollamaToolsModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(toolsModel)
                .temperature(temperature)
                .build();
    }
}
