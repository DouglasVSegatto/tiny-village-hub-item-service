package com.segatto_builder.tinyvillagehubitemservice.dto.response;

import com.segatto_builder.tinyvillagehubitemservice.model.enums.AvailabilityType;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Condition;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    /**
     * Response DTO for item data
     * Example JSON:
     {
     "id": "696881437e0814e3181e6aa1",
     "name": "Harry Potter Book 1",
     "description": "Brand new hardcover book in perfect condition",
     "type": "BOOK",
     "availabilityType": "TRADE",
     "condition": "NEW",
     "status": "ACTIVE",
     "ownerId": "550e8400-e29b-41d4-a716-446655440000",
     "ownerUsername": "john_doe",
     "ownerNeighbourhood": "Downtown",
     "ownerCity": "Seattle",
     "ownerState": "Washington",
     "ownerCountry": "USA",
     "imageUrls": [
     "https://pub-xxx.r2.dev/items/2026/696881437e0814e3181e6aa1/gallery_1.jpg"
     ],
     "properties": {},
     "createdAt": "2026-01-15T20:30:00",
     "updatedAt": "2026-01-15T20:45:00"
     }
     */

    private String id;
    private String name;
    private String description;
    private Type type;
    private AvailabilityType availabilityType;
    private Status status;
    private Condition condition;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID ownerId;
    private String ownerUsername;
    private String ownerCity;
    private String ownerNeighbourhood;
    private String ownerState;
    private String ownerCountry;
    private List<String> imageUrls;
    private Map<String, Object> properties;
}