package com.segatto_builder.tinyvillagehubitemservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IStorageService {
    String uploadFile(MultipartFile file, String folder);
    void deleteFile(String url);
    boolean fileExists(String url);
}
