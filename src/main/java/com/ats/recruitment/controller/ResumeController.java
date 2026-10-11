package com.ats.recruitment.controller;

import com.ats.recruitment.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ResumeController {

    private final FileStorageService fileStorageService;

    public ResumeController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/resumes/{filename:.+}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String filename) {
        Resource resource = fileStorageService.loadAsResource(filename);

        String contentType = "application/octet-stream";
        String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (lower.endsWith(".doc")) {
            contentType = "application/msword";
        } else if (lower.endsWith(".docx")) {
            contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }

        String disposition = lower.endsWith(".pdf") ? "inline" : "attachment";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
