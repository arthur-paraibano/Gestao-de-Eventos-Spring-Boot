package com.ingressos.api.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "true", matchIfMissing = false)
public class ImageUploadService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    private AmazonS3 s3Client;

    private AmazonS3 getS3Client() {
        if (s3Client == null) {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.fromName(region))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();
        }
        return s3Client;
    }

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode estar vazio");
        }

        // Validar tipo de arquivo
        String contentType = file.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            throw new IllegalArgumentException("Tipo de arquivo não suportado. Use apenas JPG, PNG ou GIF");
        }

        // Validar tamanho do arquivo (máximo 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 5MB");
        }

        // Gerar nome único para o arquivo
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String fileName = folder + "/" + UUID.randomUUID().toString() + fileExtension;

        try {
            // Configurar metadados
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(file.getSize());
            metadata.setCacheControl("max-age=31536000"); // Cache por 1 ano

            // Upload para S3
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    fileName,
                    file.getInputStream(),
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            getS3Client().putObject(putObjectRequest);

            // Retornar URL pública
            return getS3Client().getUrl(bucketName, fileName).toString();

        } catch (Exception e) {
            throw new IOException("Erro ao fazer upload da imagem: " + e.getMessage(), e);
        }
    }

    public String uploadEventBanner(MultipartFile file) throws IOException {
        return uploadImage(file, "event-banners");
    }

    public String uploadUserAvatar(MultipartFile file) throws IOException {
        return uploadImage(file, "user-avatars");
    }

    public String uploadEventGallery(MultipartFile file) throws IOException {
        return uploadImage(file, "event-gallery");
    }

    public boolean deleteImage(String imageUrl) {
        try {
            if (imageUrl == null || !imageUrl.contains(bucketName)) {
                return false;
            }

            // Extrair o nome do arquivo da URL
            String fileName = extractFileNameFromUrl(imageUrl);
            if (fileName != null) {
                getS3Client().deleteObject(bucketName, fileName);
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg")
                || contentType.equals("image/jpg")
                || contentType.equals("image/png")
                || contentType.equals("image/gif")
                || contentType.equals("image/webp");
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg"; // extensão padrão
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String extractFileNameFromUrl(String imageUrl) {
        try {
            // Extrair o nome do arquivo da URL do S3
            String baseUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
            if (imageUrl.startsWith(baseUrl)) {
                return imageUrl.substring(baseUrl.length());
            }

            // Formato alternativo da URL do S3
            String altBaseUrl = "https://s3." + region + ".amazonaws.com/" + bucketName + "/";
            if (imageUrl.startsWith(altBaseUrl)) {
                return imageUrl.substring(altBaseUrl.length());
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String generatePresignedUrl(String fileName, int expirationMinutes) {
        try {
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * expirationMinutes;
            expiration.setTime(expTimeMillis);

            return getS3Client().generatePresignedUrl(bucketName, fileName, expiration).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean imageExists(String imageUrl) {
        try {
            String fileName = extractFileNameFromUrl(imageUrl);
            if (fileName != null) {
                return getS3Client().doesObjectExist(bucketName, fileName);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
