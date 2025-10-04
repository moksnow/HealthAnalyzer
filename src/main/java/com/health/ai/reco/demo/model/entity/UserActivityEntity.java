package com.health.ai.reco.demo.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 4:49 PM
 */

@Entity
@Table(name = "user_activities")
@Data
@NoArgsConstructor
public class UserActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserProfileEntity user;

    @Column(name = "activity_type")
    private String activityType; // e.g., "UPLOAD", "PROCESS", "VIEW"

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "uploaded_document_id")
    private UploadedDocumentEntity document;
}
