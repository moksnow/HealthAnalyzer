package com.health.ai.reco.demo.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 4:48 PM
 */
@Entity
@Table(name = "file_analysis")
@Data
@NoArgsConstructor
public class FileAnalysisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private UploadedDocumentEntity document;

    @Lob
    @Column(name = "extracted_text", columnDefinition = "TEXT")
    private String extractedText;

    @Lob
    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}
