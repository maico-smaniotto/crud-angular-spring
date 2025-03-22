package com.maicosmaniotto.crud_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.maicosmaniotto.crud_spring.config.ValidationAdvice;
import com.maicosmaniotto.crud_spring.data.CourseTestData;
import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.PageDTO;
import com.maicosmaniotto.crud_spring.dto.mapper.CourseMapper;
import com.maicosmaniotto.crud_spring.exception.RecordNotFoundException;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

import jakarta.validation.ConstraintViolationException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Spy
    private CourseMapper courseMapper = new CourseMapper();

    @BeforeEach
    void setUp() throws Exception {
        ProxyFactory factory = new ProxyFactory(courseService);
        factory.addAdvice(new ValidationAdvice());
        courseService = (CourseService) factory.getProxy();
    }

    @Test
    @DisplayName("Should create a course successfully when valid data is provided")
    void testCreate() {
        Course course = CourseTestData.createValidCourseWithOneLesson();

        CourseDTO courseDTO = courseMapper.toDTO(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        CourseDTO createdCourse = courseService.create(courseDTO);
        assertEquals(courseDTO, createdCourse);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    @DisplayName("Should not create a course when invalid data is provided")
    void testCreateInvalid() {
        // Course with no lessons
        Course course = CourseTestData.createValidCourseWithOneLesson();
        course.getLessons().clear();
        CourseDTO courseDTO = courseMapper.toDTO(course);
        assertThrows(ConstraintViolationException.class, () -> courseService.create(courseDTO));
        verifyNoInteractions(courseRepository);

        // Course with empty name
        Course course2 = CourseTestData.createValidCourseWithOneLesson();
        course2.setName("");
        CourseDTO courseDTO2 = courseMapper.toDTO(course2);
        assertThrows(ConstraintViolationException.class, () -> courseService.create(courseDTO2));
        verifyNoInteractions(courseRepository);
    }    

    @Test
    @DisplayName("Should update a course successfully when valid data is provided")
    void testUpdate() {
        Course oldCourse = CourseTestData.createValidCourseWithOneLesson();
        Course newCourse = CourseTestData.createValidCourseWithOneLesson();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(oldCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(newCourse);

        CourseDTO newCourseDTO = courseMapper.toDTO(newCourse);

        CourseDTO updatedCourse = courseService.update(1L, newCourseDTO);
        assertEquals(newCourseDTO, updatedCourse);
        verify(courseRepository).save(any(Course.class));
        verify(courseRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when trying to update a non-existent course")
    void testUpdateNotFound() {
        Course validCourse = CourseTestData.createValidCourseWithOneLesson();
        CourseDTO validCourseDTO = courseMapper.toDTO(validCourse);
        
        when(courseRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> courseService.update(123L, validCourseDTO));
        verify(courseRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when updating course with invalid data")
    void testUpdateInvalid() {
        Course validCourse = CourseTestData.createValidCourseWithOneLesson();
        Course invalidCourse1 = CourseTestData.createValidCourseWithOneLesson();
        invalidCourse1.setName("");
        Course invalidCourse2 = CourseTestData.createValidCourseWithOneLesson();
        invalidCourse2.getLessons().clear();

        CourseDTO validCourseDTO = courseMapper.toDTO(validCourse);
        CourseDTO invalidCourseDTO1 = courseMapper.toDTO(invalidCourse1);
        CourseDTO invalidCourseDTO2 = courseMapper.toDTO(invalidCourse2);

        // Invalid id and valid data
        assertThrows(ConstraintViolationException.class, () -> courseService.update(-1L, validCourseDTO));
        // Valid id and invalid data
        assertThrows(ConstraintViolationException.class, () -> courseService.update(1L, invalidCourseDTO1));
        assertThrows(ConstraintViolationException.class, () -> courseService.update(1L, invalidCourseDTO2));
        verifyNoInteractions(courseRepository);        
    }


    @Test
    @DisplayName("Should delete a course successfully")
    void testDelete() {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        courseService.delete(1L);
        verify(courseRepository).delete(any(Course.class));
        verify(courseRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when trying to delete a non-existing course")
    void testDeleteNotFound() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> courseService.delete(1L));
        verify(courseRepository).findById(anyLong());
    }    

    @Test
    @DisplayName("Should throw ConstraintViolationException when trying to delete a course with invalid id")
    void testDeleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> courseService.delete(-1L));
        verifyNoInteractions(courseRepository);
    }

    @Test
    @DisplayName("Should return a course by id")
    void testFindById() {
        Course course = CourseTestData.createValidCourseWithOneLesson();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        CourseDTO courseDTO = courseService.findById(1L);        
        assertEquals(courseMapper.toDTO(course), courseDTO);
        verify(courseRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw NotFoundException when course is not found")
    void testFindByIdNotFound() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> courseService.findById(1L));
        verify(courseRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when course id is invalid")
    void testFindByIdInvalidId() {
        assertThrows(ConstraintViolationException.class, () -> courseService.findById(null));
        assertThrows(ConstraintViolationException.class, () -> courseService.findById(0L));
        assertThrows(ConstraintViolationException.class, () -> courseService.findById(-1L));
    }

    @Test
    @DisplayName("Should return a course page with only one course and two lessons")
    void testList() {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        CourseTestData.insertLesson(course, "Lesson 2", "Oslquz5_UNY");
        
        int pageNumber = 0;
        int pageSize = 50;

        Page<Course> mockPage = new PageImpl<>(Collections.singletonList(course));
        when(courseRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        PageDTO<CourseDTO> pageDTO = courseService.list(pageNumber, pageSize);
        
        verify(courseRepository).findAll(any(PageRequest.class));      
        assertEquals(1, pageDTO.content().size());

        CourseDTO courseDTO = pageDTO.content().get(0);
        assertEquals(2, courseDTO.lessons().size());
    }
}
