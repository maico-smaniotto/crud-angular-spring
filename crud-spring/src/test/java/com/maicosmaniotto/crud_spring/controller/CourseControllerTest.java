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
    private CourseMapper mapper = new CourseMapper();

    @BeforeEach
    void setUp() {
        ProxyFactory factory = new ProxyFactory(courseController);
        factory.addAdvice(new ValidationAdvice());
        courseController = (CourseController) factory.getProxy();
    }

    @Test
    @DisplayName("Should return a list of courses in JSON format")
    void testList() throws Exception {
        Course c = CourseTestData.createValidCourseWithOneLesson();
        CourseDTO dto = mapper.toDTO(c);
        List<CourseDTO> courses = List.of(dto);
        PageDTO<CourseDTO> page = new PageDTO<>(courses, 1, 1, 1, 0);

        when(courseService.list(anyInt(), anyInt())).thenReturn(page);
        MockMvcBuilders.standaloneSetup(courseController)
            .build()
            .perform(MockMvcRequestBuilders.get(API))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content", hasSize(courses.size())))
            .andExpect(jsonPath("totalElements", is(1)))
            .andExpect(jsonPath("$.content[0]._id", is(dto.id()), Long.class))
            .andExpect(jsonPath("$.content[0].name", is(dto.name())))
            .andExpect(jsonPath("$.content[0].category", is(dto.category())))
            .andExpect(jsonPath("$.content[0].lessons", hasSize(dto.lessons().size())))
            .andExpect(jsonPath("$.content[0].lessons[0]._id", is(dto.lessons().get(0).id()), Long.class));

    }
}
