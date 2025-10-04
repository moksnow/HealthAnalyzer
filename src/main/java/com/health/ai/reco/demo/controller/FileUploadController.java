package com.health.ai.reco.demo.controller;

import com.health.ai.reco.demo.service.OpenRouterService;
import com.health.ai.reco.demo.util.TextExtractorUtil;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author M_Khandan
 * Date: 6/1/2025
 * Time: 7:08 PM
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final OpenRouterService openRouterService;

    public FileUploadController(OpenRouterService openRouterService) {
        this.openRouterService = openRouterService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload_", file.getOriginalFilename());
            file.transferTo(tempFile);
            String text = TextExtractorUtil.extractText(tempFile);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Screenshot uploaded successfully");
            response.put("text", text);
            return ResponseEntity.ok(response);

        } catch (IOException | TesseractException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(value = "/reportExplain", consumes = {"multipart/form-data"})
    public ResponseEntity<String> reportExplain(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload_", file.getOriginalFilename());
            file.transferTo(tempFile);
            String text = TextExtractorUtil.extractText(tempFile);
            return ResponseEntity.ok(openRouterService.chat("You are a medical translator and summarizer.  \n" +
                    "Given the following MRI or scan report in English, please summarize the main findings clearly and concisely in exactly 3 sentences in Persian (Farsi): \n"
                    + text));
        } catch (IOException | TesseractException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
