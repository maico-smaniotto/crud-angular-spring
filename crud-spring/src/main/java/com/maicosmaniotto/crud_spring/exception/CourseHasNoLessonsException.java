package com.maicosmaniotto.crud_spring.exception;

public class CourseHasNoLessonsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CourseHasNoLessonsException(String name) {
        super("Course \"" + name + "\" has no lessons.");
    }
}
