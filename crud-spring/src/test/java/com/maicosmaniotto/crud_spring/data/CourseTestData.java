package com.maicosmaniotto.crud_spring.data;

import com.maicosmaniotto.crud_spring.enums.Category;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.model.Lesson;

public class CourseTestData {
    
    public static Course createValidCourseWithOneLesson() {
        Course course = new Course();
        course.setName("Course 1");
        course.setCategory(Category.BACKEND);
        insertLesson(course, "Lesson 1", "xyz123");
        return course;
    }

    public static void insertLesson(Course course, String title, String videoCode) {
        Lesson lesson = new Lesson();        
        lesson.setTitle(title);
        lesson.setVideoCode(videoCode);
        lesson.setCourse(course);
        course.getLessons().add(lesson);
    }

}
