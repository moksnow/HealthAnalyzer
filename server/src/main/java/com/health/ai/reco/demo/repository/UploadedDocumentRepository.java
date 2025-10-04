package com.health.ai.reco.demo.repository;

import com.health.ai.reco.demo.model.entity.UploadedDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 5:59 PM
 */
public interface UploadedDocumentRepository extends JpaRepository<UploadedDocumentEntity, Long> {
    List<UploadedDocumentEntity> findByUploadedBy_Id(Long userId);

}
