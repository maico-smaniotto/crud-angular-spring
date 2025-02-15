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
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-courses',
  imports: [CommonModule, AppMaterialModule, SharedModule, CoursesListComponent],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent implements OnInit {
  courses$: Observable<Course[]> | null = null;

  constructor(
    private readonly coursesService: CoursesService,
    private readonly dialog: MatDialog,
    private readonly _snackBar: MatSnackBar,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.refresh();
  }

refresh() {
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

  onEdit(obj: Course) {
    this.router.navigate(['edit', obj._id], { relativeTo: this.route });
  }

  onRemove(obj: Course) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Remover curso',
        message: `Deseja remover o curso ${obj.name}?`
      }
    });

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.coursesService.delete(obj._id)
        .subscribe(() => {
          this.refresh();
          this._snackBar.open(
            'Curso removido',
            '',
            {
              duration: 3000,
              verticalPosition: 'top',
              horizontalPosition: 'center'
            }
          );
        },
        err => {
          this.onError('Erro ao remover curso.');
        });
      }
    });

  }
}
