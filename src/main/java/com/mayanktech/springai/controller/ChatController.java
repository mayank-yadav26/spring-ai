package com.mayanktech.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
public class ChatController {

    private static final String SYSTEM_MESSAGE = """
            You are a Internal IT and provide
            Question Answer to IT policies. Be concise. If you don't know the answer,
            try to make up an answer only related to IT.If question is not related to IT policies,
            politely inform them that you are designed to answer only IT related questions.
            """;

    private final ChatClient gemmaChatClient;
    private final ChatClient mistralChatClient;

    public ChatController(@Qualifier("gemmaChatClient") ChatClient gemmaChatClient,
            @Qualifier("mistralChatClient") ChatClient mistralChatClient) {
        this.gemmaChatClient = gemmaChatClient;
        this.mistralChatClient = mistralChatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message,
            @RequestParam(value = "model", required = false) String model, @RequestParam("username") String username) {
        ChatClient selectedClient;

        // Use gemma model if 'gemma' is mentioned in the model parameter
        if (model != null && model.toLowerCase().contains("gemma")) {
            selectedClient = gemmaChatClient;
        } else {
            // Default to mistral for all other cases
            selectedClient = mistralChatClient;
        }

        // return selectedClient.prompt(message).call().content();
        return selectedClient.prompt()
                .system(SYSTEM_MESSAGE)
                .user(message)
                .advisors(
                    advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
                )
                .call().content();
    }

}