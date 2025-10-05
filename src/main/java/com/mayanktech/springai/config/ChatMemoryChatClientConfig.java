package com.mayanktech.springai.config;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mayanktech.springai.advisors.TokenUsageAuditAdvisor;
import com.mayanktech.springai.rag.PIIMaskingDocumentPostProcessor;

@Configuration
public class ChatMemoryChatClientConfig {

    @Bean("chatMemoryChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory
    ,RetrievalAugmentationAdvisor  retrievalAugmentationAdvisor
    ) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        var options = new OllamaOptions();
        options.setModel("gemma3:4b");
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor,tokenUsageAdvisor
                        ,retrievalAugmentationAdvisor
                        ))
                .defaultOptions(options)
                .build();
    }

    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder) {
        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(TranslationQueryTransformer.builder()
                        .chatClientBuilder(chatClientBuilder.clone())
                        .targetLanguage("english").build())
                .documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(vectorStore)
                        .topK(3).similarityThreshold(0.5).build())
                .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder()) // Masking of sensitive info
                .build();
    }
}
