package com.segatto_builder.tinyvillagehubitemservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDto<T> {

    /**
     * Paginated response wrapper
     * Example JSON:
     {
         "items": [
             {
                 "id": "696881437e0814e3181e6aa1",
                 "name": "Harry Potter Book 1",
                 "description": "Brand new hardcover book",
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
                 "imageUrls": ["https://tinyvillagehub.douglasvsegatto.workers.dev/items/2026/696881437e0814e3181e6aa1/gallery_1.jpg"],
                 "properties": {},
                 "createdAt": "2026-01-15T20:30:00",
                 "updatedAt": "2026-01-15T20:45:00"
             },
             {
                 "id": "696881437e0814e3181e6aa2",
                 "name": "Vintage Bicycle",
                 "description": "Classic road bike in good condition",
                 "type": "SPORTS",
                 "availabilityType": "SELL",
                 "condition": "USED",
                 "status": "ACTIVE",
                 "ownerId": "550e8400-e29b-41d4-a716-446655440000",
                 "ownerUsername": "john_doe",
                 "ownerNeighbourhood": "Downtown",
                 "ownerCity": "Seattle",
                 "ownerState": "Washington",
                 "ownerCountry": "USA",
                 "imageUrls": ["https://tinyvillagehub.douglasvsegatto.workers.dev/items/2026/696881437e0814e3181e6aa2/gallery_1.jpg"],
                 "properties": {},
                 "createdAt": "2026-01-16T10:15:00",
                 "updatedAt": "2026-01-16T10:15:00"
             }
         ],
         "page": 0,
         "size": 10,
         "totalElements": 45,
         "totalPages": 5,
         "hasNext": true,
         "statusCount": {
             "ACTIVE": 10,
             "INACTIVE": 5,
             "PENDING": 3,
             "COMPLETED": 10
         }
     }
     */

    private List<T> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private Map<String, Long> statusCount;
}
