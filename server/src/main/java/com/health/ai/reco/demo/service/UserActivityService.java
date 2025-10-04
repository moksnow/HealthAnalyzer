package com.health.ai.reco.demo.service;

import com.health.ai.reco.demo.model.entity.UploadedDocumentEntity;
import com.health.ai.reco.demo.model.entity.UserActivityEntity;
import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import com.health.ai.reco.demo.repository.UploadedDocumentRepository;
import com.health.ai.reco.demo.repository.UserActivityRepository;
import com.health.ai.reco.demo.repository.UserProfileRepository;
import com.health.ai.reco.demo.service.mapper.UserActivityMapper;
import com.health.ai.reco.demo.service.vo.UserActivityVO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:16 PM
 */
@Service
@Log4j2
public class UserActivityService {

    private final UserActivityRepository repository;
    private final UserProfileRepository userProfileRepository;
    private final UploadedDocumentRepository uploadedDocumentRepository;

    public UserActivityService(UserActivityRepository repository, UserProfileRepository userProfileRepository, UploadedDocumentRepository uploadedDocumentRepository) {
        this.repository = repository;
        this.userProfileRepository = userProfileRepository;
        this.uploadedDocumentRepository = uploadedDocumentRepository;
    }

    public UserActivityVO log(String activityType, Long userId, Long uploadedDocumentId) {
        UploadedDocumentEntity documentEntity = uploadedDocumentRepository.findById(uploadedDocumentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        UserProfileEntity user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        UserActivityEntity entity = new UserActivityEntity();
        entity.setUser(user);
        entity.setActivityType(activityType);
        entity.setTimestamp(LocalDateTime.now());
        entity.setDocument(documentEntity);

        return UserActivityMapper.toVO(repository.save(entity));
    }

    public List<UserActivityVO> getAll() {
        return repository.findAll().stream()
                .map(UserActivityMapper::toVO)
                .collect(Collectors.toList());
    }

    public List<UserActivityVO> getByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(UserActivityMapper::toVO)
                .collect(Collectors.toList());
    }

    public List<UserActivityVO> getActivitiesByUserId(Long userId) {
        List<UserActivityEntity> entities = repository.findByUserId(userId);
        return entities.stream()
                .map(UserActivityMapper::toVO)
                .collect(Collectors.toList());
    }
}
