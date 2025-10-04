package com.health.ai.reco.demo.service.mapper;

import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import com.health.ai.reco.demo.service.vo.UserProfileVO;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:06 PM
 */
public class UserProfileMapper {

    public static UserProfileVO toVO(UserProfileEntity entity) {
        if (entity == null) return null;
        UserProfileVO dto = new UserProfileVO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getEmail());
        dto.setPhoneNumber(entity.getEmail());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static UserProfileEntity toEntity(UserProfileVO dto) {
        if (dto == null) return null;
        UserProfileEntity entity = new UserProfileEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setFullName(dto.getFullName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}

