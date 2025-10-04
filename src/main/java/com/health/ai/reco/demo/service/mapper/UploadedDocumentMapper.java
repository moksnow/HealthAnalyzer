package com.health.ai.reco.demo.service.mapper;

import com.health.ai.reco.demo.model.entity.UploadedDocumentEntity;
import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import com.health.ai.reco.demo.service.vo.UploadedDocumentVO;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:06 PM
 */
public class UploadedDocumentMapper {
    public static UploadedDocumentVO toVO(UploadedDocumentEntity entity) {
        if (entity == null) return null;
        UploadedDocumentVO dto = new UploadedDocumentVO();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setUploadDate(entity.getUploadedAt());
        dto.setUserProfileId(entity.getUploadedBy().getId());
        dto.setFileName(entity.getFileName());
        dto.setExtractedText(entity.getExtractedText());
        return dto;
    }

    public static UploadedDocumentEntity toEntity(UploadedDocumentVO dto) {
        if (dto == null) return null;
        UploadedDocumentEntity entity = new UploadedDocumentEntity();
        entity.setId(dto.getId());
        entity.setFileName(dto.getFileName());
        entity.setUploadedAt(dto.getUploadDate());
        entity.setFileName(dto.getFileName());
        entity.setExtractedText(dto.getExtractedText());

        UserProfileEntity user = new UserProfileEntity();
        user.setId(dto.getUserProfileId());
        entity.setUploadedBy(user);
        return entity;
    }
}
