package com.health.ai.reco.demo.controller;

import com.health.ai.reco.demo.model.entity.UploadedDocumentEntity;
import com.health.ai.reco.demo.service.UploadedDocumentService;
import com.health.ai.reco.demo.service.UserActivityService;
import com.health.ai.reco.demo.service.vo.DocumentDetailsVO;
import com.health.ai.reco.demo.service.vo.UploadedDocumentVO;
import com.health.ai.reco.demo.service.vo.UserActivityVO;
import com.health.ai.reco.demo.util.TextExtractorUtil;
import jakarta.persistence.EntityNotFoundException;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:27 PM
 */

@RestController
@RequestMapping("/api/documents")
public class UploadedDocumentController {

    private final UploadedDocumentService uploadedDocumentService;
    private final UserActivityService userActivityService;

    public UploadedDocumentController(UploadedDocumentService uploadedDocumentService, UserActivityService userActivityService) {
        this.uploadedDocumentService = uploadedDocumentService;
        this.userActivityService = userActivityService;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<UploadedDocumentVO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userProfileId") Long userProfileId
    ) throws IOException, TesseractException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + File.separator + fileName);
        Files.createDirectories(path.getParent()); // اطمینان از وجود پوشه
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        File tempFile = File.createTempFile("upload_", file.getOriginalFilename());
        file.transferTo(tempFile);
        String text = TextExtractorUtil.extractText(tempFile);
        // استخراج متن از فایل (فرض: فایل txt هست)
        String extractedText = new String(text.getBytes(), StandardCharsets.UTF_8);

        UploadedDocumentVO vo = new UploadedDocumentVO();
        vo.setFileName(fileName);
        vo.setUploadDate(LocalDateTime.now());
        vo.setUserProfileId(userProfileId);
        vo.setExtractedText(extractedText); // باید به VO اضافه بشه

        UploadedDocumentVO created = uploadedDocumentService.upload(vo);

        userActivityService.log("UPLOAD", userProfileId, created.getId()); // جزئیات پایین‌تر

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadFileById(@PathVariable Long documentId) throws IOException {
        UploadedDocumentVO document = uploadedDocumentService.getById(documentId);

        String fileName = document.getFileName(); // از دیتابیس بخون
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<UploadedDocumentVO> getDocument(@PathVariable Long documentId) {
        UploadedDocumentVO document = uploadedDocumentService.getById(documentId);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UploadedDocumentVO>> getDocumentsByUser(@PathVariable Long userId) {
        List<UploadedDocumentVO> documents = uploadedDocumentService.getDocumentsByUserId(userId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<DocumentDetailsVO> getDocumentDetails(@PathVariable Long id) {
        return ResponseEntity.ok(uploadedDocumentService.getDocumentDetails(id));
    }
}
