package com.segatto_builder.tinyvillagehubitemservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    String uploadFile(MultipartFile file, String folder);

    void deleteFile(String url);

    boolean fileExists(String url);
}
