package com.maicosmaniotto.crud_spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CourseDTO(
    @JsonProperty("_id")    
    Long id, 

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    String name, 

    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 30, message = "Category can be at most 30 characters")
    @Pattern(regexp = "Front-end|Back-end|Database", message = "Category must be one of Front-end, Back-end or Database")
    String category
) { }
