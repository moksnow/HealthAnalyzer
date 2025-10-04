package com.health.ai.reco.demo.service.vo;

import lombok.Data;

import java.util.List;

/**
 * @author M_Khandan
 * Date: 6/8/2025
 * Time: 11:10 AM
 */
@Data
public class DocumentDetailsVO {
    private UploadedDocumentVO document;
    private List<FileAnalysisVO> analyses;
    private List<UserActivityVO> activities; // Optional
}
