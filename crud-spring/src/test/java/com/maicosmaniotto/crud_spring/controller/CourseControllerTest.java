package com.maicosmaniotto.crud_spring.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

import com.maicosmaniotto.crud_spring.config.ValidationAdvice;
import com.maicosmaniotto.crud_spring.data.CourseTestData;
import com.maicosmaniotto.crud_spring.dto.CourseDTO;
import com.maicosmaniotto.crud_spring.dto.PageDTO;
import com.maicosmaniotto.crud_spring.dto.mapper.CourseMapper;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.service.CourseService;

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
}
