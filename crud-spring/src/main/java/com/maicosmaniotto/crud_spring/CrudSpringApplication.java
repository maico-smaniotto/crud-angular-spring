package com.maicosmaniotto.crud_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.repository.CourseRepository;

@SpringBootApplication
public class CrudSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudSpringApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(CourseRepository repository) {
		return args -> {
			repository.deleteAll();
			
			Course c;

			c = new Course();
			c.setName("Angular");
			c.setCategory("Front-end");
			repository.save(c);

			c = new Course();
			c.setName("React");
			c.setCategory("Front-end");
			repository.save(c);
			
			c = new Course();
			c.setName("Java");
			c.setCategory("Back-end");
			repository.save(c);

			System.out.println("Database initialized");
		};
	}
}
