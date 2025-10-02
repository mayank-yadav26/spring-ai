package com.mayanktech.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Bean("gemmaChatClient")
    public ChatClient gemmaChatClient(OllamaChatModel ollamaChatModel) {
        var options = new OllamaOptions();
        options.setModel("gemma3:4b");
        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(options)
                .build();
    }

    @Bean("mistralChatClient")
    public ChatClient mistralChatClient(OllamaChatModel ollamaChatModel) {
        var options = new OllamaOptions();
        options.setModel("mistral:latest");
        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(options)
                .build();
    }
}