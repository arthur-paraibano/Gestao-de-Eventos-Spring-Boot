package com.ingressos.api.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "false", matchIfMissing = true)
public class LocalImageUploadService {

    @Value("${local.upload.dir:uploads}")
    private String uploadDir;
    
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode estar vazio");
        }
        
        // Criar diretório se não existir
        Path dirPath = Paths.get(uploadDir, folder);
        Files.createDirectories(dirPath);
        
        // Gerar nome único para o arquivo
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        // Salvar arquivo
        Path filePath = dirPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);
        
        // Retornar URL local
        return "/uploads/" + folder + "/" + filename;
    }
    
    public String uploadEventBanner(MultipartFile file) throws IOException {
        return uploadImage(file, "event-banners");
    }
    
    public String uploadUserAvatar(MultipartFile file) throws IOException {
        return uploadImage(file, "user-avatars");
    }
}