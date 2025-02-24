package com.maicosmaniotto.crud_spring.dto;

import java.util.List;

public record PageDTO<T>(
    List<T> content,
    long totalElements,
    int totalPages,
    int pageSize,
    int page
) { }
