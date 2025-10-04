package com.health.ai.reco.demo.service.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:09 PM
 */
@Data
public class UserActivityVO {
    private Long id;
    private String action;
    private LocalDateTime timestamp;
    private Long userProfileId;
}
