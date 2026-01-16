package com.segatto_builder.tinyvillagehubitemservice.controller;

import com.segatto_builder.tinyvillagehubitemservice.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items/{itemId}/images")
@RequiredArgsConstructor
public class ImageController {

    private final IService itemService;

    @PostMapping
    public ResponseEntity<String> uploadImage(
            @PathVariable String itemId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String userRole) {
        String imageUrl = itemService.addImage(itemId, file, userId, userRole);
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/{index}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable String itemId,
            @PathVariable int index,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String userRole) {
        itemService.removeImage(itemId, index, userId, userRole);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getImages(@PathVariable String itemId) {
        List<String> images = itemService.getImages(itemId);
        return ResponseEntity.ok(images);
    }
}
