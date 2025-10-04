package com.health.ai.reco.demo.repository;

import com.health.ai.reco.demo.model.entity.FileAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:00 PM
 */
public interface FileAnalysisRepository extends JpaRepository<FileAnalysisEntity, Long> {

    List<FileAnalysisEntity> findByDocument_Id(Long documentId);

}
