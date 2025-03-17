package com.maicosmaniotto.crud_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.PageDTO;
import com.maicosmaniotto.crud_spring.dto.mapper.CourseMapper;
import com.maicosmaniotto.crud_spring.enums.Category;
import com.maicosmaniotto.crud_spring.exception.CourseHasNoLessonsException;
import com.maicosmaniotto.crud_spring.exception.RecordNotFoundException;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.model.Lesson;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService service;

    @Mock
    private CourseRepository repository;

    @Spy
    private CourseMapper mapper = new CourseMapper();

    @Test
    void testCreate() {

    }

    @Test
    void testDelete() {

    }

    @Test
    @DisplayName("Should return a course by id")
    void testFindById() {
        Course c = new Course();
        c.setName("Angular");
        c.setCategory(Category.FRONTENT);

        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(c));
        CourseDTO dto = service.findById(1L);        
        assertEquals(mapper.toDTO(c), dto);
        Mockito.verify(repository).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Should throw NotFoundException when course is not found")
    void testFindByIdNotFound() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(RecordNotFoundException.class, () -> service.findById(1L));
        Mockito.verify(repository).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Should throw exception when course id is invalid")
    void testFindByIdInvalidId() {
        assertThrows(ConstraintViolationException.class, () -> service.findById(null));
        assertThrows(ConstraintViolationException.class, () -> service.findById(0L));
        assertThrows(ConstraintViolationException.class, () -> service.findById(-1L));
    }

    @Test
    @DisplayName("Should return a course page with only one course and two lessons")
    void testList() {
        Course c;
        
        c = new Course();
        c.setName("Angular");
        c.setCategory(Category.FRONTENT);

        String title1 = "Aula 1 - Introdução";
        String videoCode1 = "Nb4uxLxdvxo";
        insertLesson(c, title1, videoCode1);

        String title2 = "Aula 2 - Primeiros passos";
        String videoCode2 = "Oslquz5_UNY";
        insertLesson(c, title2, videoCode2);
        
        int pageNumber = 0;
        int pageSize = 50;

        Page<Course> mockPage = new PageImpl<>(Collections.singletonList(c));
        Mockito.when(repository.findAll(Mockito.any(PageRequest.class))).thenReturn(mockPage);

        PageDTO<CourseDTO> pageDTO = service.list(pageNumber, pageSize);
        
        Mockito.verify(repository).findAll(Mockito.any(PageRequest.class));      
        assertEquals(1, pageDTO.content().size());

        CourseDTO courseDTO = pageDTO.content().get(0);
        assertEquals("Angular", courseDTO.name());
        assertEquals(Category.FRONTENT.toString(), courseDTO.category());
        assertEquals(2, courseDTO.lessons().size());
        assertEquals(title1, courseDTO.lessons().get(0).title());
        assertEquals(title2, courseDTO.lessons().get(1).title());
    }

    private void insertLesson(Course course, String title, String videoCode) {
        Lesson lesson = new Lesson();        
        lesson.setTitle(title);
        lesson.setVideoCode(videoCode);
        lesson.setCourse(course);
        course.getLessons().add(lesson);
    }

    @Test
    void testUpdate() {

    }

    @Test
    @DisplayName("Should throw exception when saving course with no lesson")
    void shouldThrowExceptionWhenSavingCourseWithNoLesson() {
        Course c = new Course();
        c.setName("Angular");
        c.setCategory(Category.FRONTENT);
        c.getLessons().clear();
        
        CourseDTO courseDTO = mapper.toDTO(c);

        Assertions.assertThrows(CourseHasNoLessonsException.class, () -> service.create(courseDTO));
    }
}
