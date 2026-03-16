package com.mdotm.petapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for updating an existing pet.
 * All fields follow the same validation rules as creation.
 */
public record UpdatePetRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Species is required")
        String species,

        @Min(value = 0, message = "Age must be 0 or greater")
        Integer age,

        String ownerName
) {}
