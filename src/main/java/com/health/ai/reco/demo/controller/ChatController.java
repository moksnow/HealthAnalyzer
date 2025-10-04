package com.health.ai.reco.demo.controller;

import com.health.ai.reco.demo.service.OpenRouterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author M_Khandan
 * Date: 6/2/2025
 * Time: 6:26 PM
 */

@RestController
@RequestMapping("/chat")
public class ChatController {

    //    private final ChatGptService chatGptService;
    private final OpenRouterService openRouterService;

    public ChatController(OpenRouterService openRouterService) {
        this.openRouterService = openRouterService;
    }


//    @GetMapping
//    public String ask(@RequestParam String prompt) {
//        return chatGptService.ask(prompt);
//    }


    @GetMapping("/or")
    public String chat(@RequestParam String message) {
        return openRouterService.chat(message);
    }
}
