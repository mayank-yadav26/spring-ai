package com.mayanktech.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    private static final String DEFAULT_SYSTEM = """
            You are a Internal HR and provide
            Question Answer to HR policies. Be concise. If you don't know the answer,
            try to make up an answer only related to HR.If question is not related to HR policies,
            politely inform them that you are designed to answer only HR related questions.
            """;

    @Bean("gemmaChatClient")
    public ChatClient gemmaChatClient(OllamaChatModel ollamaChatModel) {
        var options = new OllamaOptions();
        options.setModel("gemma3:4b");
        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(options)
                .defaultSystem(DEFAULT_SYSTEM)
                .defaultUser("How can you help me?")
                .build();
    }

    @Bean("mistralChatClient")
    public ChatClient mistralChatClient(OllamaChatModel ollamaChatModel) {
        var options = new OllamaOptions();
        options.setModel("mistral:latest");
        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(options)
                .defaultSystem(DEFAULT_SYSTEM)
                .defaultUser("How can you help me?")
                .build();
    }
}