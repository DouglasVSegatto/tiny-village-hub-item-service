package com.segatto_builder.tinyvillagehubitemservice.model.entity;

import com.segatto_builder.tinyvillagehubitemservice.model.enums.AvailabilityType;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "items")
@Data
@NoArgsConstructor
public class Item {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Type is required")
    private Type type;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Availability type is required")
    private AvailabilityType availabilityType;

    @NotNull(message = "Owner ID is required")
    @Field("owner_id")
    private UUID ownerId;

    @Field("owner_neighbourhood")
    private String ownerNeighbourhood;

    @Field("owner_city")
    private String ownerCity;

    @Field("owner_state")
    private String ownerState;

    @Field("owner_country")
    private String ownerCountry;

    @Field("image_urls")
    private List<String> imageUrls = new ArrayList<>();

    // Dynamic properties for different item types
    private Map<String, Object> properties = new HashMap<>();

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    // Image management methods
    public void addImageUrl(String url) {
        if (imageUrls.size() >= 5) {
            throw new IllegalStateException("Maximum 5 images allowed");
        }
        imageUrls.add(url);
    }

    public String removeImageUrl(int index) {
        if (index < 0 || index >= imageUrls.size()) {
            throw new IndexOutOfBoundsException("Invalid image index: " + index);
        }
        return imageUrls.remove(index);
    }
}
