package com.health.ai.reco.demo.model.dto.login;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * @author M_Khandan
 * Date: 6/10/2025
 * Time: 5:10 PM
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
}
