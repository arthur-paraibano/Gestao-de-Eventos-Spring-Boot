package com.ingressos.api.controller;

import com.ingressos.api.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageUploadController {

    @Autowired(required = false)
    private ImageUploadService imageUploadService;
    
    @Value("${aws.s3.enabled:false}")
    private boolean s3Enabled;

    @PostMapping("/upload/event-banner")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadEventBanner(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        if (!s3Enabled) {
            response.put("success", false);
            response.put("message", "Upload de imagens não está disponível no momento");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
        
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Arquivo não pode estar vazio");
                return ResponseEntity.badRequest().body(response);
            }

            String imageUrl = imageUploadService.uploadEventBanner(file);
            
            response.put("success", true);
            response.put("message", "Banner do evento enviado com sucesso");
            response.put("imageUrl", imageUrl);
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/upload/user-avatar")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadUserAvatar(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Arquivo não pode estar vazio");
                return ResponseEntity.badRequest().body(response);
            }

            String imageUrl = imageUploadService.uploadUserAvatar(file);
            
            response.put("success", true);
            response.put("message", "Avatar do usuário enviado com sucesso");
            response.put("imageUrl", imageUrl);
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/upload/event-gallery")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadEventGallery(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Arquivo não pode estar vazio");
                return ResponseEntity.badRequest().body(response);
            }

            String imageUrl = imageUploadService.uploadEventGallery(file);
            
            response.put("success", true);
            response.put("message", "Imagem da galeria enviada com sucesso");
            response.put("imageUrl", imageUrl);
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "URL da imagem é obrigatória");
                return ResponseEntity.badRequest().body(response);
            }

            boolean deleted = imageUploadService.deleteImage(imageUrl);
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "Imagem deletada com sucesso");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Não foi possível deletar a imagem");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> checkImageExists(@RequestParam("imageUrl") String imageUrl) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "URL da imagem é obrigatória");
                return ResponseEntity.badRequest().body(response);
            }

            boolean exists = imageUploadService.imageExists(imageUrl);
            
            response.put("success", true);
            response.put("exists", exists);
            response.put("imageUrl", imageUrl);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/presigned-url")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> generatePresignedUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam(value = "expirationMinutes", defaultValue = "60") int expirationMinutes) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nome do arquivo é obrigatório");
                return ResponseEntity.badRequest().body(response);
            }

            if (expirationMinutes < 1 || expirationMinutes > 1440) { // máximo 24 horas
                response.put("success", false);
                response.put("message", "Tempo de expiração deve estar entre 1 e 1440 minutos");
                return ResponseEntity.badRequest().body(response);
            }

            String presignedUrl = imageUploadService.generatePresignedUrl(fileName, expirationMinutes);
            
            if (presignedUrl != null) {
                response.put("success", true);
                response.put("presignedUrl", presignedUrl);
                response.put("expirationMinutes", expirationMinutes);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Não foi possível gerar URL pré-assinada");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/upload/multiple")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadMultipleImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folder", defaultValue = "event-gallery") String folder) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (files == null || files.length == 0) {
                response.put("success", false);
                response.put("message", "Nenhum arquivo foi enviado");
                return ResponseEntity.badRequest().body(response);
            }

            if (files.length > 10) {
                response.put("success", false);
                response.put("message", "Máximo de 10 arquivos por upload");
                return ResponseEntity.badRequest().body(response);
            }

            java.util.List<Map<String, Object>> uploadResults = new java.util.ArrayList<>();
            
            for (MultipartFile file : files) {
                Map<String, Object> fileResult = new HashMap<>();
                
                try {
                    if (!file.isEmpty()) {
                        String imageUrl = imageUploadService.uploadImage(file, folder);
                        fileResult.put("success", true);
                        fileResult.put("fileName", file.getOriginalFilename());
                        fileResult.put("imageUrl", imageUrl);
                        fileResult.put("fileSize", file.getSize());
                    } else {
                        fileResult.put("success", false);
                        fileResult.put("fileName", file.getOriginalFilename());
                        fileResult.put("message", "Arquivo vazio");
                    }
                } catch (Exception e) {
                    fileResult.put("success", false);
                    fileResult.put("fileName", file.getOriginalFilename());
                    fileResult.put("message", e.getMessage());
                }
                
                uploadResults.add(fileResult);
            }
            
            response.put("success", true);
            response.put("message", "Upload múltiplo processado");
            response.put("results", uploadResults);
            response.put("totalFiles", files.length);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}