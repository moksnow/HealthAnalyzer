package com.health.ai.reco.demo.service;

import com.health.ai.reco.demo.model.entity.FileAnalysisEntity;
import com.health.ai.reco.demo.model.entity.UploadedDocumentEntity;
import com.health.ai.reco.demo.model.entity.UserActivityEntity;
import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import com.health.ai.reco.demo.repository.FileAnalysisRepository;
import com.health.ai.reco.demo.repository.UploadedDocumentRepository;
import com.health.ai.reco.demo.repository.UserActivityRepository;
import com.health.ai.reco.demo.service.mapper.FileAnalysisMapper;
import com.health.ai.reco.demo.service.vo.FileAnalysisVO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:21 PM
 */

@Service
@Log4j2
public class FileAnalysisService {
    private final FileAnalysisRepository repository;
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final UserActivityRepository userActivityRepository;
    private final OpenRouterService openRouterService;

    public FileAnalysisService(FileAnalysisRepository repository, UploadedDocumentRepository uploadedDocumentRepository, UserActivityRepository userActivityRepository, OpenRouterService openRouterService) {
        this.repository = repository;
        this.uploadedDocumentRepository = uploadedDocumentRepository;
        this.userActivityRepository = userActivityRepository;
        this.openRouterService = openRouterService;
    }

    public FileAnalysisVO analyze(FileAnalysisVO dto) {
        FileAnalysisEntity entity = FileAnalysisMapper.toEntity(dto);
        return FileAnalysisMapper.toVO(repository.save(entity));
    }

    public FileAnalysisVO getById(Long id) {
        return repository.findById(id)
                .map(FileAnalysisMapper::toVO)
                .orElse(null);
    }

    public List<FileAnalysisVO> getAll() {
        return repository.findAll().stream()
                .map(FileAnalysisMapper::toVO)
                .collect(Collectors.toList());
    }


    public List<FileAnalysisVO> getFileAnalysesByDocumentId(Long documentId) {
        List<FileAnalysisEntity> entities = repository.findByDocument_Id(documentId);
        return entities.stream()
                .map(FileAnalysisMapper::toVO)
                .collect(Collectors.toList());
    }

    public FileAnalysisVO analyzeAndSave(FileAnalysisVO vo) {
        UploadedDocumentEntity doc = uploadedDocumentRepository.findById(vo.getUploadedDocumentId())
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        String editedText = vo.getExtractedText();

        String summary = analyzeText(editedText);

        FileAnalysisEntity analysis = new FileAnalysisEntity();
        analysis.setDocument(doc);
        analysis.setProcessedAt(LocalDateTime.now());
        analysis.setSummary(summary);
        analysis.setExtractedText(editedText);
        repository.save(analysis);

        UserProfileEntity user = doc.getUploadedBy();

        if (!Objects.equals(doc.getExtractedText(), editedText)) {
            UserActivityEntity editActivity = new UserActivityEntity();
            editActivity.setActivityType("EDIT");
            editActivity.setTimestamp(LocalDateTime.now());
            editActivity.setUser(user);
            editActivity.setDocument(doc);
            userActivityRepository.save(editActivity);
        }

        UserActivityEntity activity = new UserActivityEntity();
        activity.setActivityType("PROCESS");
        activity.setTimestamp(LocalDateTime.now());
        activity.setUser(user);
        activity.setDocument(doc);
        userActivityRepository.save(activity);

        return FileAnalysisMapper.toVO(analysis);
    }


    private String analyzeText(String text) {
        if (text == null || text.isBlank()) return "No content provided.";
        return openRouterService.chat(text);
    }

}
