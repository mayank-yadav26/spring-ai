package com.mayanktech.springai.controller;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient gemmaChatClient;
    private final ChatClient mistralChatClient;

    public ChatController(@Qualifier("gemmaChatClient") ChatClient gemmaChatClient,
                         @Qualifier("mistralChatClient") ChatClient mistralChatClient) {
        this.gemmaChatClient = gemmaChatClient;
        this.mistralChatClient = mistralChatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message,
                      @RequestParam(value = "model", required = false) String model) {
        ChatClient selectedClient;
        
        // Use gemma model if 'gemma' is mentioned in the model parameter
        if (model != null && model.toLowerCase().contains("gemma")) {
            selectedClient = gemmaChatClient;
        } else {
            // Default to mistral for all other cases
            selectedClient = mistralChatClient;
        }
        
        return selectedClient.prompt(message).call().content();
    }

}