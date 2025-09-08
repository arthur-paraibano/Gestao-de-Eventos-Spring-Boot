package com.ingressos.api.service;

import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "false", matchIfMissing = true)
public class MockImageUploadService extends ImageUploadService {

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        // Apenas simula o upload e retorna uma URL fictícia
        return "https://example.com/mock-image-" + System.currentTimeMillis() + ".jpg";
    }
    
    @Override
    public String uploadEventBanner(MultipartFile file) {
        return uploadImage(file, "event-banners");
    }
    
    @Override
    public String uploadUserAvatar(MultipartFile file) {
        return uploadImage(file, "user-avatars");
    }
}