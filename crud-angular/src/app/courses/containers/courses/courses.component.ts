import { Component, OnInit } from '@angular/core';
import { Course } from '../../model/course';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from '../../../shared/app-material/app-material.module';
import { SharedModule } from '../../../shared/shared.module';
import { CoursesService } from '../../services/courses.service';
import { catchError, Observable, of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { CoursesListComponent } from '../../components/courses-list/courses-list.component';

@Component({
  selector: 'app-courses',
  imports: [CommonModule, AppMaterialModule, SharedModule, CoursesListComponent],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent implements OnInit {
  courses$: Observable<Course[]>;

  constructor(
    private readonly coursesService: CoursesService,
    private readonly dialog: MatDialog,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    // retorna um observable
    this.courses$ = this.coursesService.list()
      .pipe(
        catchError(err => {
          console.log(err.message);
          this.onError('Erro ao carregar cursos.');
          return of([]);
        })
      );

    // se quisesse converter para array (neste caso this.courses$ seria um array de Course)
    // porém não é necessário pois o angular já faz isso (dataSource da mat-table aceita um observable)
    // this.courses = this.coursesService.list().subscribe(courses => {this.courses$ = courses});
  }

  onError(errorMsg: string) {
    this.dialog.open(ErrorDialogComponent, {
      data: errorMsg
    });
  }

  ngOnInit() {
    // poderia ser aqui no ngOnInit ou no construtor
    // porém no modo strict do angular (definido em tsconfig.json)
    // obriga que tudo seja inicializado por isso é necessário no construtor
    // this.courses = this.coursesService.list();
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

}
