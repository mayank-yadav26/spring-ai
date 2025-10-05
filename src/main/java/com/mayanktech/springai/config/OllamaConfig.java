package com.mayanktech.springai.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
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
    public ChatClient gemmaChatClient(OllamaChatModel ollamaChatModel, ChatMemory chatMemory) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        var options = new OllamaOptions();
        options.setModel("gemma3:4b");
        options.setTemperature(0.7); // Adjust temperature for more creative responses, more the value more the creativity and randomness.
        options.setMaxTokens(500); // Adjust max tokens as per your requirement.
        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(options)
                .defaultSystem(DEFAULT_SYSTEM)
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor))
                .defaultUser("How can you help me?")
                .build();
    }

    @Bean("mistralChatClient")
    public ChatClient mistralChatClient(OllamaChatModel ollamaChatModel,ChatMemory chatMemory) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        var options = new OllamaOptions();
        options.setModel("mistral:latest");
        options.setTemperature(0.7); // Adjust temperature for more creative responses, more the value more the creativity and randomness.
        options.setMaxTokens(500); // Adjust max tokens as per your requirement.
        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(options)
                .defaultSystem(DEFAULT_SYSTEM)
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor))
                .defaultUser("How can you help me?")
                .build();
    }

    /**
     * Chat memory bean to store the conversation history in the database.
     * It changes default maxMessages from 20 to 10.
     * @param jdbcChatMemoryRepository
     * @return
     */
    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }
}