package com.maicosmaniotto.crud_spring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    private final CourseRepository courseRepository;

	public CourseController(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@GetMapping
    public List<Course> list() {
        return courseRepository.findAll();
    }
}
