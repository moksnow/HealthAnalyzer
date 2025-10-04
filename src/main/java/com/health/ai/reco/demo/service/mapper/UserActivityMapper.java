package com.health.ai.reco.demo.service.mapper;

import com.health.ai.reco.demo.model.entity.UserActivityEntity;
import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import com.health.ai.reco.demo.service.vo.UserActivityVO;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:07 PM
 */
public class UserActivityMapper {

    public static UserActivityVO toVO(UserActivityEntity entity) {
        if (entity == null) return null;

        UserActivityVO dto = new UserActivityVO();
        dto.setId(entity.getId());
        dto.setAction(entity.getActivityType());
        dto.setTimestamp(entity.getTimestamp());
        dto.setUserProfileId(entity.getUser() != null ? entity.getUser().getId() : null);
        return dto;
    }

    public static UserActivityEntity toEntity(UserActivityVO dto, UserProfileEntity userProfile) {
        if (dto == null) return null;

        UserActivityEntity entity = new UserActivityEntity();
        entity.setId(dto.getId());
        entity.setActivityType(dto.getAction());
        entity.setTimestamp(dto.getTimestamp());
        entity.setUser(userProfile);
        return entity;
    }
}

