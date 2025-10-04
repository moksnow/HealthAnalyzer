package com.health.ai.reco.demo.service.mapper;

import com.health.ai.reco.demo.model.entity.FileAnalysisEntity;
import com.health.ai.reco.demo.model.entity.UploadedDocumentEntity;
import com.health.ai.reco.demo.service.vo.FileAnalysisVO;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:06 PM
 */
public class FileAnalysisMapper {

    public static FileAnalysisVO toVO(FileAnalysisEntity entity) {
        if (entity == null) return null;
        FileAnalysisVO dto = new FileAnalysisVO();
        dto.setId(entity.getId());
        dto.setAnalysisResult(entity.getSummary());
        dto.setAnalyzedAt(entity.getProcessedAt());
        dto.setExtractedText(entity.getExtractedText());
        dto.setUploadedDocumentId(entity.getDocument().getId());
        return dto;
    }

    public static FileAnalysisEntity toEntity(FileAnalysisVO dto) {
        if (dto == null) return null;
        FileAnalysisEntity entity = new FileAnalysisEntity();
        entity.setId(dto.getId());
        entity.setSummary(dto.getAnalysisResult());
        entity.setProcessedAt(dto.getAnalyzedAt());
        entity.setExtractedText(dto.getExtractedText());

        UploadedDocumentEntity doc = new UploadedDocumentEntity();
        doc.setId(dto.getUploadedDocumentId());
        entity.setDocument(doc);
        return entity;
    }
}


