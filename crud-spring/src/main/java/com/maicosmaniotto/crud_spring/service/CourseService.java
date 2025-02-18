package com.maicosmaniotto.crud_spring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.maicosmaniotto.crud_spring.exception.RecordNotFoundException;
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

    public Course findById(@NotNull @Positive Long id) {
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public Course create(@Valid Course obj) {
        return repository.save(obj);
    }

    public Course update(@NotNull @Positive Long id, @Valid Course obj) {
        return repository.findById(id)
            .map(found -> {
                found.setName(obj.getName());
                found.setCategory(obj.getCategory());
                return repository.save(found);
            }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        repository.delete(
            repository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException(id))
        );
    }
}
