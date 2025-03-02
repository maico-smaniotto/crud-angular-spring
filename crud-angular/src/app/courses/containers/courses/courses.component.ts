import { Component, OnInit, ViewChild } from '@angular/core';
import { Course } from '../../model/course';
import { CommonModule } from '@angular/common';
import { CoursesService } from '../../services/courses.service';
import { catchError, Observable, of, tap } from 'rxjs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { CoursesListComponent } from '../../components/courses-list/courses-list.component';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { Page } from '../../model/page';
import { MatPaginator, MatPaginatorIntl, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomPaginatorIntl } from '../../../shared/custom-paginator-intl';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatTableModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule,
    MatPaginatorModule,
    ReactiveFormsModule,
    CoursesListComponent
  ],
  providers: [
    { provide: MatPaginatorIntl, useClass: CustomPaginatorIntl }
  ]
})
export class CoursesComponent implements OnInit {
  courses$: Observable<Page<Course>> | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  // variáveis podem ser usadas para inicializar a paginação e também para atualizar o componente ao mudar de página
  // neste caso não preciso pois coloquei no próprio dto estas variáveis e acesso diretamente
  // pageIndex = 0;
  // pageSize = 10;

  constructor(
    private readonly coursesService: CoursesService,
    private readonly dialog: MatDialog,
    private readonly _snackBar: MatSnackBar,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.refresh();
  }

  refresh(pageEvent: PageEvent = {pageIndex: 0, pageSize: 10, length: 0}) {
    // retorna um observable
    this.courses$ = this.coursesService.list(pageEvent.pageIndex, pageEvent.pageSize)
    .pipe(
      // tap(() => {
      //   this.pageIndex = pageEvent.pageIndex
      //   this.pageSize = pageEvent.pageSize
      // }),
      catchError(err => {
        console.log(err.message);
        this.onError('Erro ao carregar cursos.');
        return of({content: [], totalElements: 0, totalPages: 0, pageSize: 0, page: 0});
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

  onGetRangeLabel(page: number, pageSize: number, length: number) {
    if (length === 0) {
      return '0 de 0';
    }
    const startIndex = page * pageSize;
    const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
    return `${startIndex + 1} - ${endIndex} de ${length}`;
  }
}
