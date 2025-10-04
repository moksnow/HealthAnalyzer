package com.health.ai.reco.demo.service.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:09 PM
 */
@Data
public class FileAnalysisVO {
    private Long id;
    private String analysisResult;
    private LocalDateTime analyzedAt;
    private Long uploadedDocumentId;
    private String extractedText;
}
