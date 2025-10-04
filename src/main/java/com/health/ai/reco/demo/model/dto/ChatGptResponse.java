package com.health.ai.reco.demo.model.dto;

import java.util.List;

/**
 * @author M_Khandan
 * Date: 6/2/2025
 * Time: 2:41 PM
 */

public record ChatGptResponse(List<Choice> choices) {
    public record Choice(Message message) {
    }

    public record Message(String role, String content) {
    }
}
