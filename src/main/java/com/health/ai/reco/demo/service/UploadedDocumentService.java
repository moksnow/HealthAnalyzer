package com.health.ai.reco.demo.service;

import com.health.ai.reco.demo.model.entity.FileAnalysisEntity;
import com.health.ai.reco.demo.model.entity.UploadedDocumentEntity;
import com.health.ai.reco.demo.model.entity.UserActivityEntity;
import com.health.ai.reco.demo.repository.FileAnalysisRepository;
import com.health.ai.reco.demo.repository.UploadedDocumentRepository;
import com.health.ai.reco.demo.repository.UserActivityRepository;
import com.health.ai.reco.demo.service.mapper.FileAnalysisMapper;
import com.health.ai.reco.demo.service.mapper.UploadedDocumentMapper;
import com.health.ai.reco.demo.service.mapper.UserActivityMapper;
import com.health.ai.reco.demo.service.vo.DocumentDetailsVO;
import com.health.ai.reco.demo.service.vo.FileAnalysisVO;
import com.health.ai.reco.demo.service.vo.UploadedDocumentVO;
import com.health.ai.reco.demo.service.vo.UserActivityVO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:21 PM
 */
@Service
@Log4j2
public class UploadedDocumentService {
    private final UploadedDocumentRepository repository;
    private final UserActivityRepository userActivityRepository;
    private final FileAnalysisRepository fileAnalysisRepository;


    public UploadedDocumentService(UploadedDocumentRepository repository, UserActivityRepository userActivityRepository, FileAnalysisRepository fileAnalysisRepository) {
        this.repository = repository;
        this.userActivityRepository = userActivityRepository;
        this.fileAnalysisRepository = fileAnalysisRepository;
    }


    public UploadedDocumentVO upload(UploadedDocumentVO dto) {
        UploadedDocumentEntity entity = UploadedDocumentMapper.toEntity(dto);
        return UploadedDocumentMapper.toVO(repository.save(entity));
    }

        public UploadedDocumentVO getById(Long id) {
        return repository.findById(id)
                .map(UploadedDocumentMapper::toVO)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with ID: " + id));
    }

    public List<UploadedDocumentVO> getAll() {
        return repository.findAll().stream()
                .map(UploadedDocumentMapper::toVO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<UploadedDocumentVO> getDocumentsByUserId(Long userId) {
        List<UploadedDocumentEntity> entities = repository.findByUploadedBy_Id(userId);
        return entities.stream()
                .map(UploadedDocumentMapper::toVO)
                .collect(Collectors.toList());
    }

    public DocumentDetailsVO getDocumentDetails(Long documentId) {
        UploadedDocumentEntity document = repository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        UploadedDocumentVO documentVO = UploadedDocumentMapper.toVO(document);

        List<FileAnalysisEntity> analyses = fileAnalysisRepository.findByDocument_Id(documentId);
        List<FileAnalysisVO> analysisVOs = analyses.stream()
                .map(FileAnalysisMapper::toVO)
                .collect(Collectors.toList());

        List<UserActivityEntity> activities = userActivityRepository.findByDocument_Id(documentId); // اگر نیاز باشه
        List<UserActivityVO> activityVOs = activities.stream()
                .map(UserActivityMapper::toVO)
                .collect(Collectors.toList());

        DocumentDetailsVO vo = new DocumentDetailsVO();
        vo.setDocument(documentVO);
        vo.setAnalyses(analysisVOs);
        vo.setActivities(activityVOs);

        return vo;
    }

}
