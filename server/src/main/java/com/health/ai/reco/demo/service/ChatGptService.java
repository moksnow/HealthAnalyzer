package com.health.ai.reco.demo.service;


import com.health.ai.reco.demo.model.dto.ChatGptRequest;
import com.health.ai.reco.demo.model.dto.ChatGptResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * @author M_Khandan
 * Date: 6/2/2025
 * Time: 2:40 PM
 */
//@Service
public class ChatGptService {
    private final RestClient restClient;
    private final String apiKey;

    public ChatGptService(@Value("${openai.api.key}") String apiKey) {
        this.apiKey = apiKey;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String ask(String prompt) {
        ChatGptRequest request = new ChatGptRequest(
                "gpt-3.5-turbo",
                List.of(Map.of("role", "user", "content", prompt))
        );

        ChatGptResponse response = restClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(ChatGptResponse.class);

        return response.choices().get(0).message().content();
    }
}
