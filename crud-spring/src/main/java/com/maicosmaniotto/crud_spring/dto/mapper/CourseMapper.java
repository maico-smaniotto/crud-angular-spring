package com.maicosmaniotto.crud_spring.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.LessonDTO;
import com.maicosmaniotto.crud_spring.enums.converters.CategoryConverter;
import com.maicosmaniotto.crud_spring.enums.converters.RecordStatusConverter;
import com.maicosmaniotto.crud_spring.model.Course;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course entity) {
        if (entity == null) {
            return null;
        }
        
        List<LessonDTO> lessonDTOs = entity.getLessons()
            .stream()
            .map(lesson -> new LessonDTO(lesson.getId(), lesson.getTitle(), lesson.getVideoCode()))
            .toList();

        return new CourseDTO(
            entity.getId(), 
            entity.getName(), 
            entity.getCategory().toString(), 
            entity.getStatus().toString(), 
            lessonDTOs
        );
    }

    public Course toEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Course entity = new Course();
        
        if (dto.id() != null) {
            entity.setId(dto.id());
        }        
        
        entity.setName(dto.name());
        entity.setCategory(CategoryConverter.stringToEntityAttribute(dto.category()));        
        
        if (dto.status() != null) {
            entity.setStatus(RecordStatusConverter.stringToEntityAttribute(dto.status()));
        }
        
        return entity;
    }

}
