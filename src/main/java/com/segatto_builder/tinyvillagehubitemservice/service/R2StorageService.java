package com.segatto_builder.tinyvillagehubitemservice.service;

import com.segatto_builder.tinyvillagehubitemservice.config.R2Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@ConditionalOnProperty(name = "storage.provider", havingValue = "r2")
@RequiredArgsConstructor
@Slf4j
public class R2StorageService implements IStorageService {

    private final S3Client s3Client;
    private final R2Config r2Config;

    @Override
    public String uploadFile(MultipartFile file, String itemId) {
        try {

            String year = String.valueOf(java.time.Year.now().getValue());
            int imageIndex = getCurrentImageCount(itemId, year) + 1;
            String extension = getFileExtension(file.getOriginalFilename());

            String fileName = "gallery_" + System.currentTimeMillis() + extension;
            String key = "items/" + year + "/" + itemId + "/" + fileName;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(r2Config.getBucketName())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));

            return r2Config.getPublicUrl() + "/" + key;

        } catch (Exception e) {
            log.error("Failed to upload file for item {}: {}", itemId, e.getMessage());
            throw new RuntimeException("Upload failed", e);
        }
    }

    private int getCurrentImageCount(String itemId, String year) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(r2Config.getBucketName())
                .prefix("items/" + year + "/" + itemId + "/")
                .build();

        return s3Client.listObjectsV2(request).contents().size();
    }


    @Override
    public void deleteFile(String url) {
        try {
            String key = getKeyFromUrl(url);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(r2Config.getBucketName())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            log.info("Successfully deleted file: {}", key);

        } catch (Exception e) {
            log.error("Failed to delete file {}: {}", url, e.getMessage());
            throw new RuntimeException("Delete failed", e);
        }
    }

    @Override
    public boolean fileExists(String url) {
        try {
            String key = getKeyFromUrl(url);

            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(r2Config.getBucketName())
                    .key(key)
                    .build();

            s3Client.headObject(headRequest);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private String getKeyFromUrl(String url) {
        String publicUrl = r2Config.getPublicUrl();
        if (!url.startsWith(publicUrl)) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
        // +1 to remove last slash
        return url.substring(publicUrl.length() + 1);
    }


    // PRIVATE METHODS

    private String getFileExtension(String filename) {
        return filename != null && filename.contains(".")
                ? filename.substring(filename.lastIndexOf("."))
                : ".jpg";
    }

}

