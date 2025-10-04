package com.health.ai.reco.demo.service.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:08 PM
 */
@Data
public class UploadedDocumentVO {
    private Long id;
    private String fileName;
    private String extractedText;
    private LocalDateTime uploadDate;
    private Long userProfileId;
}
