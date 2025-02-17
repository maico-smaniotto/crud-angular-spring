package com.maicosmaniotto.crud_spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class CourseService {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<Course> list() {
        return repository.findAll();
    }

    public Optional<Course> findById(/*@PathVariable*/ @NotNull @Positive Long id) {
        return repository.findById(id);
    }

    public Course create(@Valid Course obj) {
        return repository.save(obj);
    }

    public Optional<Course> update(@NotNull @Positive Long id, @Valid Course obj) {
        return repository.findById(id)
            .map(found -> {
                found.setName(obj.getName());
                found.setCategory(obj.getCategory());
                return repository.save(found);
            });
    }

    public boolean delete(@NotNull @Positive Long id) {
        return repository.findById(id)
            .map(found -> {
                repository.delete(found);
                return true;
            }).orElse(false);
    }
}
