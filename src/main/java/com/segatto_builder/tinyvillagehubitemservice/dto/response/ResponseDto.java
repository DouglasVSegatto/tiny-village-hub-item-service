package com.segatto_builder.tinyvillagehubitemservice.dto.response;

import com.segatto_builder.tinyvillagehubitemservice.model.enums.AvailabilityType;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Type;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class ResponseDto {

    private String id;
    private String name;
    private String description;
    private Type type;
    private AvailabilityType availabilityType;
    private Status status;
    private UUID ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private String ownerCity;
    private String ownerNeighbourhood;
    private String ownerState;
    private String ownerCountry;
    private List<String> imageUrls;
    private Map<String, Object> properties;
}