package com.segatto_builder.tinyvillagehubitemservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDto {

    /**
     * Response DTO for image data
     *
     * Example JSON:
     {
     "url": "https://pub-fce349a2db27403aa5e224fe4f0fbbef.r2.dev/items/2026/696881437e0814e3181e6aa1/gallery_1.jpg",
     "index": 0,
     "fileName": "gallery_1.jpg",
     "uploadedAt": "2026-01-15T20:30:00"
     }
     */

    private String url;
    private int index;
    private String fileName;
    private LocalDateTime uploadedAt;
}
