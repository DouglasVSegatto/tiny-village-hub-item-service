package com.segatto_builder.tinyvillagehubitemservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDto {
    private String url;
    private int index;
    private String fileName;
    private LocalDateTime uploadedAt;
}
