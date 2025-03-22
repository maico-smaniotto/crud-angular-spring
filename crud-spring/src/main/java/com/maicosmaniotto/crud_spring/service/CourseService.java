package com.maicosmaniotto.crud_spring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.PageDTO;
import com.maicosmaniotto.crud_spring.dto.mapper.CourseMapper;
import com.maicosmaniotto.crud_spring.enums.converters.CategoryConverter;
import com.maicosmaniotto.crud_spring.enums.converters.RecordStatusConverter;
import com.maicosmaniotto.crud_spring.exception.RecordNotFoundException;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public PageDTO<CourseDTO> list(
        @PositiveOrZero    int pageNumber, 
        @Positive @Max(50) int pageSize
    ) {
        Page<Course> page = courseRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<CourseDTO> courses = page.stream().map(courseMapper::toDTO).toList();
        return new PageDTO<>(courses, page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber());
    }

    public CourseDTO findById(@NotNull @Positive Long id) {
        return courseRepository.findById(id).map(courseMapper::toDTO)
            .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public CourseDTO create(@Valid @NotNull CourseDTO courseDTO) {
        return courseMapper.toDTO(courseRepository.save(courseMapper.toEntity(courseDTO)));
    }

    public CourseDTO update(@NotNull @Positive Long id, @Valid @NotNull CourseDTO courseDTO) {
        return courseRepository.findById(id)
            .map(found -> {
                found.setName(courseDTO.name());
                found.setCategory(CategoryConverter.stringToEntityAttribute(courseDTO.category()));

                if (courseDTO.status() != null) {
                    found.setStatus(RecordStatusConverter.stringToEntityAttribute(courseDTO.status()));
                }
                // Não pode sobrescrever a lista com .setLessons() pois o Hibernate precisa da referência original para funcionar
                // found.setLessons(mapper.toEntity(courseDTO).getLessons());
                found.getLessons().clear();
                courseMapper.toEntity(courseDTO).getLessons().forEach(found.getLessons()::add);                
                return courseMapper.toDTO(courseRepository.save(found));
            }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        courseRepository.delete(
            courseRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException(id))
        );
    }
}
