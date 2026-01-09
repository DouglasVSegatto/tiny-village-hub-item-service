package com.segatto_builder.tinyvillagehubitemservice.dto.request;

import com.segatto_builder.tinyvillagehubitemservice.model.enums.AvailabilityType;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Condition;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Status;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestDto {

    /* Json Example
    {
      "name": "iPhone 15 Pro",
      "description": "Brand new iPhone 15 Pro, 256GB, Space Black. Excellent condition, includes original box and charger.",
      "type": "ELECTRONICS",
      "availabilityType": "FOR_SALE",
      "condition": "NEW",
      "status": "ACTIVE",
      "ownerNeighbourhood": "Downtown",
      "ownerCity": "Seattle",
      "ownerState": "Washington",
      "ownerCountry": "USA"
    }
    * */
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Type is required")
    private Type type;

    @NotNull(message = "Availability type is required")
    private AvailabilityType availabilityType;

    @NotNull(message = "Condition is required")
    private Condition condition;

    @NotNull(message = "Status is required")
    private Status status;

    @NotBlank(message = "Owner city is required")
    private String ownerCity;

    @NotBlank(message = "Owner neighbourhood is required")
    private String ownerNeighbourhood;

    @NotBlank(message = "Owner state is required")
    private String ownerState;

    @NotBlank(message = "Owner country is required")
    private String ownerCountry;
}
