import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder, FormControl, FormGroup } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { AppMaterialModule } from '../../../shared/app-material/app-material.module';
import { SharedModule } from '../../../shared/shared.module';
import { CoursesService } from '../../services/courses.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';
import { ActivatedRoute } from '@angular/router';
import { Course } from '../../model/course';

interface CourseForm {
  _id: FormControl<string>;
  name: FormControl<string>;
  category: FormControl<string>;
}

@Component({
  selector: 'app-course-form',
  imports: [CommonModule, AppMaterialModule, SharedModule],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent implements OnInit {

  form: FormGroup<CourseForm>;

  constructor(
    private readonly formBuilder: NonNullableFormBuilder,
    private readonly service: CoursesService,
    private readonly location: Location,
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog,
    private readonly _snackBar: MatSnackBar
  ) {
    this.form = this.formBuilder.group({
      _id: [''],
      name: [''],
      category: ['']
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.service.save(
        this.form.value
      ).subscribe({
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
    this.location.back();
  }

  private onSuccess(result: any) {
    console.log('Curso salvo com sucesso', result);
    this._snackBar.open('Curso salvo com sucesso', '', { duration: 3000 });
    this.location.back();
  }

  private onError(error: any) {
    console.error(error.message);
    this.dialog.open(ErrorDialogComponent, {
      data: 'Erro ao salvar curso'
    });
  }

  ngOnInit() {
    const course: Course = this.route.snapshot.data['course'];
    this.form.setValue({
      _id: course._id,
      name: course.name,
      category: course.category
    });
  }
}
