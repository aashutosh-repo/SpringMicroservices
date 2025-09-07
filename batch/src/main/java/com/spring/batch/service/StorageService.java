package com.spring.batch.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// storage/StorageService.java
@Service
@RequiredArgsConstructor
public class StorageService {
    private final Path root = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");

    @PostConstruct
    void init() throws IOException { Files.createDirectories(root); }

    public String store(MultipartFile file) throws IOException {
        Path dest = root.resolve(UUID.randomUUID() + "-" + file.getOriginalFilename());
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }
        return dest.toAbsolutePath().toString();
    }
}
