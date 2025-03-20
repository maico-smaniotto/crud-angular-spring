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

import com.maicosmaniotto.crud_spring.config.ValidationAdvice;
import com.maicosmaniotto.crud_spring.data.CourseTestData;
import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.PageDTO;
import com.maicosmaniotto.crud_spring.dto.mapper.CourseMapper;
import com.maicosmaniotto.crud_spring.exception.RecordNotFoundException;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService service;

    @Mock
    private CourseRepository repository;

    @Spy
    private CourseMapper mapper = new CourseMapper();

    @BeforeEach
    void setUp() throws Exception {
        ProxyFactory factory = new ProxyFactory(service);
        factory.addAdvice(new ValidationAdvice());
        service = (CourseService) factory.getProxy();
    }

    @Test
    @DisplayName("Should create a course successfully when valid data is provided")
    void testCreate() {
        Course c = CourseTestData.createValidCourseWithOneLesson();

        CourseDTO dto = mapper.toDTO(c);
        when(repository.save(any(Course.class))).thenReturn(c);
        CourseDTO createdCourse = service.create(dto);
        assertEquals(dto, createdCourse);
        verify(repository).save(any(Course.class));
    }

    @Test
    @DisplayName("Should not create a course when invalid data is provided")
    void testCreateInvalid() {
        // Course with no lessons
        Course c = CourseTestData.createValidCourseWithOneLesson();
        c.getLessons().clear();
        CourseDTO dto = mapper.toDTO(c);
        assertThrows(ConstraintViolationException.class, () -> service.create(dto));
        verifyNoInteractions(repository);

        // Course with empty name
        Course c2 = CourseTestData.createValidCourseWithOneLesson();
        c2.setName("");
        CourseDTO dto2 = mapper.toDTO(c2);
        assertThrows(ConstraintViolationException.class, () -> service.create(dto2));
        verifyNoInteractions(repository);
    }    

    @Test
    @DisplayName("Should update a course successfully when valid data is provided")
    void testUpdate() {
        Course oldCourse = CourseTestData.createValidCourseWithOneLesson();
        Course newCourse = CourseTestData.createValidCourseWithOneLesson();

        when(repository.findById(anyLong())).thenReturn(Optional.of(oldCourse));
        when(repository.save(any(Course.class))).thenReturn(newCourse);

        CourseDTO newCourseDTO = mapper.toDTO(newCourse);

        CourseDTO updatedCourse = service.update(1L, newCourseDTO);
        assertEquals(newCourseDTO, updatedCourse);
        verify(repository).save(any(Course.class));
        verify(repository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when trying to update a non-existent course")
    void testUpdateNotFound() {
        Course validCourse = CourseTestData.createValidCourseWithOneLesson();
        CourseDTO validCourseDTO = mapper.toDTO(validCourse);
        
        when(repository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> service.update(123L, validCourseDTO));
        verify(repository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when updating course with invalid data")
    void testUpdateInvalid() {
        Course validCourse = CourseTestData.createValidCourseWithOneLesson();
        Course invalidCourse1 = CourseTestData.createValidCourseWithOneLesson();
        invalidCourse1.setName("");
        Course invalidCourse2 = CourseTestData.createValidCourseWithOneLesson();
        invalidCourse2.getLessons().clear();

        CourseDTO validCourseDTO = mapper.toDTO(validCourse);
        CourseDTO invalidCourseDTO1 = mapper.toDTO(invalidCourse1);
        CourseDTO invalidCourseDTO2 = mapper.toDTO(invalidCourse2);

        // Invalid id and valid data
        assertThrows(ConstraintViolationException.class, () -> service.update(-1L, validCourseDTO));
        // Valid id and invalid data
        assertThrows(ConstraintViolationException.class, () -> service.update(1L, invalidCourseDTO1));
        assertThrows(ConstraintViolationException.class, () -> service.update(1L, invalidCourseDTO2));
        verifyNoInteractions(repository);        
    }


    @Test
    @DisplayName("Should delete a course successfully")
    void testDelete() {
        Course c = CourseTestData.createValidCourseWithOneLesson();
        when(repository.findById(anyLong())).thenReturn(Optional.of(c));
        service.delete(1L);
        verify(repository).delete(any(Course.class));
        verify(repository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when trying to delete a non-existing course")
    void testDeleteNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> service.delete(1L));
        verify(repository).findById(anyLong());
    }    

    @Test
    @DisplayName("Should throw ConstraintViolationException when trying to delete a course with invalid id")
    void testDeleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> service.delete(-1L));
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Should return a course by id")
    void testFindById() {
        Course c = CourseTestData.createValidCourseWithOneLesson();

        when(repository.findById(anyLong())).thenReturn(Optional.of(c));
        CourseDTO dto = service.findById(1L);        
        assertEquals(mapper.toDTO(c), dto);
        verify(repository).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw NotFoundException when course is not found")
    void testFindByIdNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> service.findById(1L));
        verify(repository).findById(anyLong());
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
        Course c = CourseTestData.createValidCourseWithOneLesson();
        CourseTestData.insertLesson(c, "Lesson 2", "Oslquz5_UNY");
        
        int pageNumber = 0;
        int pageSize = 50;

        Page<Course> mockPage = new PageImpl<>(Collections.singletonList(c));
        when(repository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        PageDTO<CourseDTO> pageDTO = service.list(pageNumber, pageSize);
        
        verify(repository).findAll(any(PageRequest.class));      
        assertEquals(1, pageDTO.content().size());

        CourseDTO courseDTO = pageDTO.content().get(0);
        assertEquals(2, courseDTO.lessons().size());
    }
}
