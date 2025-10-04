package com.health.ai.reco.demo.controller;

import com.health.ai.reco.demo.service.UserActivityService;
import com.health.ai.reco.demo.service.vo.UserActivityVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:39 PM
 */
@RestController
@RequestMapping("/api/user-activities")
public class UserActivityController {
    private final UserActivityService userActivityService;

    public UserActivityController(UserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserActivityVO>> getUserActivities(@PathVariable Long userId) {
        List<UserActivityVO> activities = userActivityService.getActivitiesByUserId(userId);
        return ResponseEntity.ok(activities);
    }
}
