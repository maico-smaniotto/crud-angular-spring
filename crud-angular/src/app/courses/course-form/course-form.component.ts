import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from '../../shared/app-material/app-material.module';
import { SharedModule } from '../../shared/shared.module';
import { ActivatedRoute, Router } from '@angular/router';
import { CoursesService } from '../services/courses.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../../shared/components/error-dialog/error-dialog.component';

@Component({
  selector: 'app-course-form',
  imports: [CommonModule, AppMaterialModule, SharedModule],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent {

  form: FormGroup;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly service: CoursesService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog,
    private readonly _snackBar: MatSnackBar
  ) {
    this.form = this.formBuilder.group({
      name: [null],
      category: [null]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.service.save(this.form.value).subscribe({
        next: result => {
          this.onSuccess(result);
        },
        error: error => {
          this.onError(error);
        }
      });
    }
  }

  onCancel() {
    this.router.navigate([''], { relativeTo: this.route });
  }

  private onSuccess(result: any) {
    console.log('Curso salvo com sucesso', result);
    this._snackBar.open('Curso salvo com sucesso', '', { duration: 5000 });
    this.router.navigate([''], { relativeTo: this.route });
  }

  private onError(error: any) {
    console.error(error.message);
    this.dialog.open(ErrorDialogComponent, {
      data: 'Erro ao salvar curso'
    });
  }
}
