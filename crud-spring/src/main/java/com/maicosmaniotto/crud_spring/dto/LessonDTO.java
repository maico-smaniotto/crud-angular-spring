package com.maicosmaniotto.crud_spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LessonDTO(
    @JsonProperty("_id")    
    Long id,

    @NotBlank(message = "title is required")
    @Size(min = 5, max = 100, message = "must be between 5 and 100 characters")
    String title,

    @NotBlank(message = "videoCode is required")
    @Size(min = 5, max = 20, message = "must be between 5 and 20 characters")    
    String videoCode
) { }
