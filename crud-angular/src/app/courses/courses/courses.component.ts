import { Component, OnInit } from '@angular/core';
import { Course } from '../model/course';
import { AppMaterialModule } from '../../shared/app-material/app-material.module';
import { CoursesService } from '../services/courses.service';

@Component({
  selector: 'app-courses',
  imports: [AppMaterialModule],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent implements OnInit {
  courses: Course[] = [];
  //coursesService: CoursesService;

  displayedColumns = ['name', 'category'];

  constructor(private coursesService: CoursesService) {
    //this.coursesService = new CoursesService();
    // this.courses = this.coursesService.list();
  }

  ngOnInit() {
    // pode ser aqui no ngOnInit ou no construtor
    this.courses = this.coursesService.list();
  }
}
