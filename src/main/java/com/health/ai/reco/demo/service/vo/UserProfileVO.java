package com.health.ai.reco.demo.service.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:08 PM
 */
@Data
public class UserProfileVO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private String password;
    private String fullName;
    private String phoneNumber;
}
