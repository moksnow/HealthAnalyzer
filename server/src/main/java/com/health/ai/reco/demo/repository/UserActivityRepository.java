package com.health.ai.reco.demo.repository;

import com.health.ai.reco.demo.model.entity.FileAnalysisEntity;
import com.health.ai.reco.demo.model.entity.UserActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:01 PM
 */
public interface UserActivityRepository extends JpaRepository<UserActivityEntity, Long> {
    List<UserActivityEntity> findByUserId(Long userId);
     List<UserActivityEntity> findByDocument_Id(Long documentId);
}
