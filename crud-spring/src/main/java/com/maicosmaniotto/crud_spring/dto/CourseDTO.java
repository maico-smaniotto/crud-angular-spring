package com.maicosmaniotto.crud_spring.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maicosmaniotto.crud_spring.enums.Category;
import com.maicosmaniotto.crud_spring.enums.RecordStatus;
import com.maicosmaniotto.crud_spring.enums.validation.CategoryStringSubset;
import com.maicosmaniotto.crud_spring.enums.validation.RecordStatusStringSubset;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseDTO(
    @JsonProperty("_id")    
    Long id, 

    @NotNull
    @NotBlank(message = "name is required")
    @Size(min = 1, max = 100, message = "must be between 1 and 100 characters")
    String name, 

    @NotNull
    @NotBlank(message = "category is required")
    @CategoryStringSubset(anyOf = {Category.FRONTENT, Category.BACKEND, Category.DATABASE})
    String category,

    @RecordStatusStringSubset(anyOf = {RecordStatus.ACTIVE, RecordStatus.INACTIVE})
    String status,

    @NotNull
    @NotEmpty
    @Valid
    List<LessonDTO> lessons
) { }
