package com.maicosmaniotto.crud_spring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.service.CourseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    private final CourseService service;

	public CourseController(CourseService service) {
        this.service = service;
	}

	@GetMapping
    public List<CourseDTO> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public CourseDTO findById(@PathVariable @NotNull @Positive Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CourseDTO create(@RequestBody @Valid @NotNull CourseDTO obj) {
        return service.create(obj);
    }

    @PutMapping("/{id}")
    public CourseDTO update(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid @NotNull CourseDTO obj) {
        return service.update(id, obj);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull @Positive Long id) {
        service.delete(id);
    }
}
