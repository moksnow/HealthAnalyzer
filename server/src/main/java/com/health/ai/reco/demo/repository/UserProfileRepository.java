package com.health.ai.reco.demo.repository;

import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 5:57 PM
 */
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUsername(String username);
}
