package com.segatto_builder.tinyvillagehubitemservice.dto.request;

import com.segatto_builder.tinyvillagehubitemservice.model.enums.AvailabilityType;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Condition;
import com.segatto_builder.tinyvillagehubitemservice.model.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateRequestDto {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Type type;

    @NotNull
    private AvailabilityType availabilityType;

    @NotNull
    private Condition condition;
}
