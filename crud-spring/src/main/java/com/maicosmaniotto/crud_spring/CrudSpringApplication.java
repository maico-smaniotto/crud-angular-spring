package com.maicosmaniotto.crud_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.maicosmaniotto.crud_spring.enums.Category;
import com.maicosmaniotto.crud_spring.model.Course;
import com.maicosmaniotto.crud_spring.model.Lesson;
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
			Lesson l;

			c = new Course();
			c.setName("Angular");
			c.setCategory(Category.FRONTENT);

			l = new Lesson();
			l.setTitle("CRUD Angular + Spring | 46: Curso-Aulas: Listando Aulas");
			l.setVideoCode("Nb4uxLxdvxo");
			l.setCourse(c);
			c.getLessons().add(l);

			l = new Lesson();
			l.setTitle("CRUD Angular + Spring | 50: Curso-Aulas: FormArray HTML");
			l.setVideoCode("Oslquz5_UNY");
			l.setCourse(c);

			c.getLessons().add(l);
			repository.save(c);
						
			c = new Course();
			c.setName("React");
			c.setCategory(Category.FRONTENT);

			l = new Lesson();
			l.setTitle("React | 01: Introdução");
			l.setVideoCode("Oslquz5_UNY");
			l.setCourse(c);

			c.getLessons().add(l);			
			repository.save(c);
			
			c = new Course();
			c.setName("Java");
			c.setCategory(Category.BACKEND);

			l = new Lesson();
			l.setTitle("Spring | 01: Introdução ao Spring");
			l.setVideoCode("Oslquz5_UNY");
			l.setCourse(c);

			c.getLessons().add(l);
			repository.save(c);

			System.out.println("Database initialized");
		};
	}
}
