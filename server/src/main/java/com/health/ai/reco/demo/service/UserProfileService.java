package com.health.ai.reco.demo.service;

import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import com.health.ai.reco.demo.repository.UserProfileRepository;
import com.health.ai.reco.demo.service.mapper.UserProfileMapper;
import com.health.ai.reco.demo.service.vo.UserProfileVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:22 PM
 */
@Service
@Log4j2
public class UserProfileService {
    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserProfileVO create(UserProfileVO dto) {
        UserProfileEntity entity = UserProfileMapper.toEntity(dto);
        return UserProfileMapper.toVO(repository.save(entity));
    }

    public UserProfileVO getById(Long id) {
        return repository.findById(id)
                .map(UserProfileMapper::toVO)
                .orElse(null);
    }

    public List<UserProfileVO> getAll() {
        return repository.findAll().stream()
                .map(UserProfileMapper::toVO)
                .collect(Collectors.toList());
    }

    public UserProfileVO update(Long id, UserProfileVO dto) {
        if (!repository.existsById(id)) return null;
        UserProfileEntity entity = UserProfileMapper.toEntity(dto);
        entity.setId(id);
        return UserProfileMapper.toVO(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
