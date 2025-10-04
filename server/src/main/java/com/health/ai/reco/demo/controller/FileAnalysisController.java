package com.health.ai.reco.demo.controller;

import com.health.ai.reco.demo.service.FileAnalysisService;
import com.health.ai.reco.demo.service.vo.FileAnalysisVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author M_Khandan
 * Date: 6/7/2025
 * Time: 6:32 PM
 */
@RestController
@RequestMapping("/api/file-analyses")
public class FileAnalysisController {
    private final FileAnalysisService fileAnalysisService;

    public FileAnalysisController(FileAnalysisService fileAnalysisService) {
        this.fileAnalysisService = fileAnalysisService;
    }

    @GetMapping("/{analysesId}")
    public ResponseEntity<FileAnalysisVO> getById(@PathVariable Long analysesId) {
        FileAnalysisVO analyses = fileAnalysisService.getById(analysesId);
        return ResponseEntity.ok(analyses);
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<FileAnalysisVO>> getAnalysisByDocument(@PathVariable Long documentId) {
        List<FileAnalysisVO> analyses = fileAnalysisService.getFileAnalysesByDocumentId(documentId);
        return ResponseEntity.ok(analyses);
    }

    @PostMapping
    public ResponseEntity<FileAnalysisVO> analyze(@RequestBody FileAnalysisVO vo) {
        FileAnalysisVO result = fileAnalysisService.analyzeAndSave(vo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
