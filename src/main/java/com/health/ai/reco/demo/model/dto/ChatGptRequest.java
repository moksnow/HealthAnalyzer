package com.health.ai.reco.demo.model.dto;

/**
 * @author M_Khandan
 * Date: 6/2/2025
 * Time: 2:41 PM
 */

import java.util.List;
import java.util.Map;

public record ChatGptRequest(
        String model,
        List<Map<String, String>> messages
) {
}