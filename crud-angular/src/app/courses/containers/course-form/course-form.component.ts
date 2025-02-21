import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder, FormGroup, Validators, UntypedFormArray } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { AppMaterialModule } from '../../../shared/app-material/app-material.module';
import { SharedModule } from '../../../shared/shared.module';
import { CoursesService } from '../../services/courses.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';
import { ActivatedRoute } from '@angular/router';
import { Course } from '../../model/course';
import { Lesson } from '../../model/lesson';

@Component({
  selector: 'app-course-form',
  imports: [CommonModule, AppMaterialModule, SharedModule],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent implements OnInit {

  form!: FormGroup;

  private readonly NAME_MAX_LENGTH = 100;

  constructor(
    private readonly formBuilder: NonNullableFormBuilder,
    private readonly service: CoursesService,
    private readonly location: Location,
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog,
    private readonly _snackBar: MatSnackBar
  ) { }

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

  getErrorMessage(form: FormGroup, fieldName: string) {
    const field = form.get(fieldName);

    if (field?.hasError('required')) {
      return 'Campo obrigatório';
    }
    if (field?.hasError('minlength')) {
      const minLength = field.errors ? field?.errors['minlength']['requiredLength'] : 0;
      return `Campo deve ter no mínimo ${minLength} caracteres`;
    }
    if (field?.hasError('maxlength')) {
      const maxLength = field.errors ? field?.errors['maxlength']['requiredLength'] : 0;
      return `Campo pode ter no máximo ${maxLength} caracteres`;
    }
    return 'Campo inválido';
  }

  getMaxLength(fieldName: string): number {
    if (fieldName === 'name') {
      return this.NAME_MAX_LENGTH;
    }
    return 0;
  }

  getLessonsFormArray() {
    return (<UntypedFormArray>this.form.get('lessons')).controls;
  }

  isFormArrayRequired() {
    const lessons = this.form.get('lessons') as UntypedFormArray;
    return !lessons.valid && lessons.hasError('required') && lessons.touched;
  }

  ngOnInit() {
    const course: Course = this.route.snapshot.data['course'];

    this.form = this.formBuilder.group({
      _id: [course._id],
      name: [course.name, [Validators.required, Validators.minLength(1), Validators.maxLength(this.NAME_MAX_LENGTH)]],
      category: [course.category, [Validators.required, Validators.minLength(1), Validators.maxLength(30)]],
      lessons: this.formBuilder.array(this.retrieveLessons(course), Validators.required)
    });
  }

  private retrieveLessons(course: Course) {
    const lessons = [];
    if (course?.lessons) {
      course.lessons.forEach(lesson => lessons.push(this.createLesson(lesson)));
    } else {
      lessons.push(this.createLesson());
    }
    return lessons;
  }

  private createLesson(lesson: Lesson = {id: '', title: '', videoCode: ''}) {
    return this.formBuilder.group({
      id: [lesson.id],
      title: [lesson.title, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      videoCode: [lesson.videoCode, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(20)
      ]]
    });
  }
}
