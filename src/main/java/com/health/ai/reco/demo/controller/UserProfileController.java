package com.health.ai.reco.demo.controller;

import com.health.ai.reco.demo.service.UserProfileService;
import com.health.ai.reco.demo.service.vo.UserProfileVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:26 PM
 */
@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfileVO> createUserProfile(@RequestBody UserProfileVO dto) {
        UserProfileVO created = userProfileService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileVO> getUserProfile(@PathVariable Long id) {
        UserProfileVO dto = userProfileService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileVO> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileVO dto) {
        UserProfileVO updated = userProfileService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
