package com.ats.recruitment.service;

import com.ats.recruitment.exception.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads/resumes");
    private final List<String> allowedExtensions = Arrays.asList("pdf", "doc", "docx");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage folder for resumes", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new BadRequestException("Invalid filename sequence: " + originalFilename);
        }

        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        if (!allowedExtensions.contains(fileExtension)) {
            throw new BadRequestException("Invalid file type. Only PDF, DOC, and DOCX formats are allowed.");
        }

        String storedFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

        try {
            Path destinationFile = this.rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        }
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BadRequestException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new BadRequestException("Could not read file: " + filename);
        }
    }

    public void storeSampleResume(String filename, byte[] content) {
        try {
            Path destinationFile = this.rootLocation.resolve(filename).normalize().toAbsolutePath();
            if (!Files.exists(destinationFile)) {
                Files.write(destinationFile, content);
            }
        } catch (IOException e) {
            // Log or ignore for sample
        }
    }
}
