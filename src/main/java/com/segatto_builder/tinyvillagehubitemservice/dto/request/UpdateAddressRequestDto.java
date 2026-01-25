package com.segatto_builder.tinyvillagehubitemservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateAddressRequestDto {

    /**
     * Example JSON:
     {
     "neighborhood": "Vila Madalena",
     "city": "São Paulo",
     "state": "SP",
     "country": "Brazil"
     }
     */

    @NotBlank(message = "Neighborhood is required")
    private String neighborhood;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;
}
