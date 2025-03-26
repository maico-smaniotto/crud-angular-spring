package com.maicosmaniotto.crud_spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicosmaniotto.crud_spring.config.ValidationAdvice;
import com.maicosmaniotto.crud_spring.data.CourseTestData;
import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.PageDTO;
import com.maicosmaniotto.crud_spring.dto.mapper.CourseMapper;
import com.maicosmaniotto.crud_spring.exception.RecordNotFoundException;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.service.CourseService;

import jakarta.servlet.ServletException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    private static final String API = "/api/courses";
    private static final String API_ID = "/api/courses/{id}";

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @Spy
    private CourseMapper courseMapper = new CourseMapper();

    @BeforeEach
    void setUp() {
        ProxyFactory factory = new ProxyFactory(courseController);
        factory.addAdvice(new ValidationAdvice());
        courseController = (CourseController) factory.getProxy();
    }

    @Test
    @DisplayName("Should return a list of courses in JSON format")
    void testList() throws Exception {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        CourseDTO courseDTO = courseMapper.toDTO(course);
        List<CourseDTO> courses = List.of(courseDTO);
        PageDTO<CourseDTO> page = new PageDTO<>(courses, 1, 1, 1, 0);

        when(courseService.list(anyInt(), anyInt())).thenReturn(page);

        var requestBuilder = MockMvcRequestBuilders.get(API);
        MockMvcBuilders.standaloneSetup(courseController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content", hasSize(courses.size())))
            .andExpect(jsonPath("totalElements", is(1)))
            .andExpect(jsonPath("$.content[0]._id", is(courseDTO.id()), Long.class))
            .andExpect(jsonPath("$.content[0].name", is(courseDTO.name())))
            .andExpect(jsonPath("$.content[0].category", is(courseDTO.category())))
            .andExpect(jsonPath("$.content[0].lessons", hasSize(courseDTO.lessons().size())))
            .andExpect(jsonPath("$.content[0].lessons[0]._id", is(courseDTO.lessons().get(0).id()), Long.class));

    }

    @Test
    @DisplayName("Should return a course by id in JSON format")
    void testFindById() throws Exception {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDTO(course);

        when(courseService.findById(courseDTO.id())).thenReturn(courseDTO);

        var requestBuilder = MockMvcRequestBuilders.get(API_ID, course.getId());
        MockMvcBuilders.standaloneSetup(courseController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("_id", is(courseDTO.id()), Long.class))
            .andExpect(jsonPath("name", is(courseDTO.name())))
            .andExpect(jsonPath("category", is(courseDTO.category())))
            .andExpect(jsonPath("lessons", hasSize(courseDTO.lessons().size())))
            .andExpect(jsonPath("lessons[0]._id", is(courseDTO.lessons().get(0).id()), Long.class));
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when id is negative")
    void testFindByIdNegative() {              
        var requestBuilder = MockMvcRequestBuilders.get(API_ID, -1L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        });
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when course is not found")
    void testFindByIdNotFound() {
        when(courseService.findById(anyLong())).thenThrow(RecordNotFoundException.class);

        var requestBuilder = MockMvcRequestBuilders.get(API_ID, 1L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
        });
    }

    @Test
    @DisplayName("Should create a new course")
    void testCreate() throws Exception {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        CourseDTO courseDTO = courseMapper.toDTO(course);

        when(courseService.create(courseDTO)).thenReturn(courseDTO);

        var contentJson = new ObjectMapper().writeValueAsString(courseDTO);
        var requestBuilder = MockMvcRequestBuilders.post(API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentJson);
        MockMvcBuilders.standaloneSetup(courseController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("_id", is(courseDTO.id()), Long.class))
            .andExpect(jsonPath("name", is(courseDTO.name())))
            .andExpect(jsonPath("category", is(courseDTO.category())))
            .andExpect(jsonPath("lessons", hasSize(courseDTO.lessons().size())))
            .andExpect(jsonPath("lessons[0]._id", is(courseDTO.lessons().get(0).id()), Long.class));
    }

    @Test
    @DisplayName("Should return bad request when trying to create a course with invalid data")
    void testCreateInvalid() throws Exception {
        List<CourseDTO> courses = new ArrayList<>();
        Course course;

        course = CourseTestData.createValidCourseWithOneLesson();
        course.setName("");
        courses.add(courseMapper.toDTO(course));

        course = CourseTestData.createValidCourseWithOneLesson();
        course.getLessons().clear();
        courses.add(courseMapper.toDTO(course));
        
        for (CourseDTO courseDTO : courses) {
            var contentJson = new ObjectMapper().writeValueAsString(courseDTO);
            var requestBuilder = MockMvcRequestBuilders.post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson);
            MockMvcBuilders.standaloneSetup(courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("Should update a course")
    void testUpdate() throws Exception {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDTO(course);

        when(courseService.update(anyLong(), any())).thenReturn(courseDTO);

        var contentJson = new ObjectMapper().writeValueAsString(courseDTO);
        var requestBuilder = MockMvcRequestBuilders.put(API_ID, course.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentJson);
        MockMvcBuilders.standaloneSetup(courseController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("_id", is(courseDTO.id()), Long.class))
            .andExpect(jsonPath("name", is(courseDTO.name())))
            .andExpect(jsonPath("category", is(courseDTO.category())))
            .andExpect(jsonPath("lessons", hasSize(courseDTO.lessons().size())))
            .andExpect(jsonPath("lessons[0]._id", is(courseDTO.lessons().get(0).id()), Long.class));
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when trying to update a course that does not exist")
    void testUpdateNotFound() throws Exception {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDTO(course);

        when(courseService.update(anyLong(), any())).thenThrow(RecordNotFoundException.class);

        var contentJson = new ObjectMapper().writeValueAsString(courseDTO);
        var requestBuilder = MockMvcRequestBuilders.put(API_ID, course.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentJson);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
        });
    }

    @Test
    @DisplayName("Should return bad request when trying to update a course with invalid data")
    void testUpdateInvalid() throws Exception {
        Map<Long, CourseDTO> courses = new HashMap<>();
        Course course;

        // Valid id and invalid data
        course = CourseTestData.createValidCourseWithOneLesson();
        course.setName("");
        courses.put(1L, courseMapper.toDTO(course));
        course = CourseTestData.createValidCourseWithOneLesson();
        course.getLessons().clear();
        courses.put(2L, courseMapper.toDTO(course));

        for (Map.Entry<Long, CourseDTO> entry : courses.entrySet()) {
            var contentJson = new ObjectMapper().writeValueAsString(entry.getValue());
            var requestBuilder = MockMvcRequestBuilders.put(API_ID, entry.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson);
            MockMvcBuilders.standaloneSetup(courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        }

        courses.clear();

        // Invalid id and valid data
        course = CourseTestData.createValidCourseWithOneLesson();
        courses.put(0L, courseMapper.toDTO(course));
        course = CourseTestData.createValidCourseWithOneLesson();
        courses.put(-1L, courseMapper.toDTO(course));

        for (Map.Entry<Long, CourseDTO> entry : courses.entrySet()) {            
            var contentJson = new ObjectMapper().writeValueAsString(entry.getValue());
            var requestBuilder = MockMvcRequestBuilders.put(API_ID, entry.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson);
            assertThrows(ServletException.class, () -> {
                MockMvcBuilders.standaloneSetup(courseController)
                    .build()
                    .perform(requestBuilder)
                    .andExpect(status().isBadRequest());
            });
        }
    }

    @Test
    @DisplayName("Should delete a course")
    void testDelete() throws Exception {
        doNothing().when(courseService).delete(anyLong());

        var requestBuilder = MockMvcRequestBuilders.delete(API_ID, 1L);
        MockMvcBuilders.standaloneSetup(courseController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should throw RecordNotFoundException when trying to delete a course that does not exist")
    void testDeleteNotFound() {
        doThrow(RecordNotFoundException.class).when(courseService).delete(anyLong());
        var requestBuilder = MockMvcRequestBuilders.delete(API_ID, 1L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
        });
    }

    @Test
    @DisplayName("Should return bad request when trying to delete a course with invalid data")
    void testDeleteInvalid() {
        var requestBuilder = MockMvcRequestBuilders.delete(API_ID, 0L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        });
    }
}
