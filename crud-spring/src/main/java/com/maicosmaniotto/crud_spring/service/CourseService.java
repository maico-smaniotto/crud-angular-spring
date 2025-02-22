package com.maicosmaniotto.crud_spring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.mapper.CourseMapper;
import com.maicosmaniotto.crud_spring.enums.converters.CategoryConverter;
import com.maicosmaniotto.crud_spring.enums.converters.RecordStatusConverter;
import com.maicosmaniotto.crud_spring.exception.RecordNotFoundException;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class CourseService {
    private final CourseRepository repository;
    private final CourseMapper mapper;

    public CourseService(CourseRepository repository, CourseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CourseDTO> list() {
        return repository.findAll()
            .stream()
            .map(mapper::toDTO)
            .toList();
    }

    public CourseDTO findById(@NotNull @Positive Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public CourseDTO create(@Valid @NotNull CourseDTO obj) {
        return mapper.toDTO(repository.save(mapper.toEntity(obj)));
    }

    public CourseDTO update(@NotNull @Positive Long id, @Valid @NotNull CourseDTO courseDTO) {
        return repository.findById(id)
            .map(found -> {
                found.setName(courseDTO.name());
                found.setCategory(CategoryConverter.stringToEntityAttribute(courseDTO.category()));

                if (courseDTO.status() != null) {
                    found.setStatus(RecordStatusConverter.stringToEntityAttribute(courseDTO.status()));
                }
                // Não pode sobrescrever a lista com .setLessons() pois o Hibernate precisa da referência original para funcionar
                // found.setLessons(mapper.toEntity(courseDTO).getLessons());
                found.getLessons().clear();
                mapper.toEntity(courseDTO).getLessons().forEach(found.getLessons()::add);                
                return mapper.toDTO(repository.save(found));
            }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        repository.delete(
            repository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException(id))
        );
    }
}
