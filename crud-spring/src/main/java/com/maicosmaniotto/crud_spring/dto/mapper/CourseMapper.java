package com.maicosmaniotto.crud_spring.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.LessonDTO;
import com.maicosmaniotto.crud_spring.enums.converters.CategoryConverter;
import com.maicosmaniotto.crud_spring.enums.converters.RecordStatusConverter;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.model.Lesson;

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

    public Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }
        
        Course course = new Course();
        
        if (courseDTO.id() != null) {
            course.setId(courseDTO.id());
        }        
        
        course.setName(courseDTO.name());
        course.setCategory(CategoryConverter.stringToEntityAttribute(courseDTO.category()));        
        
        if (courseDTO.status() != null) {
            course.setStatus(RecordStatusConverter.stringToEntityAttribute(courseDTO.status()));
        }

        course.getLessons().clear();
        courseDTO.lessons().stream().forEach(lessonDTO -> {
            var lesson = new Lesson();
            lesson.setId(lessonDTO.id());
            lesson.setTitle(lessonDTO.title());
            lesson.setVideoCode(lessonDTO.videoCode());
            lesson.setCourse(course);

            course.getLessons().add(lesson);
        });
        
        return course;
    }

}
