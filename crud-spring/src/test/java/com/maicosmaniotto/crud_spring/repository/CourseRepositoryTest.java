package com.maicosmaniotto.crud_spring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.maicosmaniotto.crud_spring.data.CourseTestData;
import com.maicosmaniotto.crud_spring.model.Course;

@ActiveProfiles("test")
@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("Should find all courses with lessons")
    void testFindAll() {
        
        Course course;
        course = CourseTestData.createValidCourseWithOneLesson();        
        course.setName("Course 1");
        entityManager.persist(course);

        course = CourseTestData.createValidCourseWithOneLesson();        
        course.setName("Course 2");
        entityManager.persist(course);

        Page<Course> coursePage = courseRepository.findAll(PageRequest.of(0, 10));
        
        assertThat(coursePage)
            .isNotNull()
            .hasSize(2);
        
        assertThat(coursePage.getContent()).isNotEmpty();

        coursePage.getContent().forEach(c -> {
            assertThat(c.getLessons()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("Should find course by id")
    void testFindById() {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        Course savedCourse = entityManager.persist(course);

        Optional<Course> courseFound = courseRepository.findById(savedCourse.getId());

        assertTrue(courseFound.isPresent());
        assertThat(courseFound.get()).isEqualTo(savedCourse);
    }

    @Test
    @DisplayName("Should save a new course")
    void testSave() {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        Course savedCourse = courseRepository.save(course);

        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo(course.getName());
    }

    @Test
    @DisplayName("Should update an existing course")
    void testUpdate() {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        Course savedCourse = entityManager.persist(course);

        savedCourse.setName("New Name");
        Course updatedCourse = courseRepository.save(savedCourse);

        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getId()).isEqualTo(savedCourse.getId());
        assertThat(updatedCourse.getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("Should delete a course")
    void testDelete() {
        Course course = CourseTestData.createValidCourseWithOneLesson();
        Course savedCourse = entityManager.persist(course);

        courseRepository.delete(savedCourse);

        Optional<Course> courseFound = courseRepository.findById(savedCourse.getId());

        assertTrue(courseFound.isEmpty());
    }

}
