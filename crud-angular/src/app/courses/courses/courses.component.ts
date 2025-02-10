import { Component, OnInit } from '@angular/core';
import { Course } from '../model/course';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from '../../shared/app-material/app-material.module';
import { CoursesService } from '../services/courses.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-courses',
  imports: [CommonModule, AppMaterialModule],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent implements OnInit {
  courses$: Observable<Course[]>;

  displayedColumns = ['name', 'category'];

  constructor(private coursesService: CoursesService) {
    // retorna um observable
    this.courses$ = this.coursesService.list();

    // se quisesse converter para array (neste caso this.courses$ seria um array de Course)
    // porém não é necessário pois o angular já faz isso (dataSource da mat-table aceita um observable)
    // this.courses = this.coursesService.list().subscribe(courses => {this.courses$ = courses});
  }

  ngOnInit() {
    // poderia ser aqui no ngOnInit ou no construtor
    // porém no modo strict do angular (definido em tsconfig.json)
    // obriga que tudo seja inicializado por isso é necessário no construtor
    // this.courses = this.coursesService.list();
  }
}
