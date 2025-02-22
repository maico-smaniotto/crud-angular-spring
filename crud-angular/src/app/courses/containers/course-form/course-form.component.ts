import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder, FormGroup, Validators, UntypedFormArray, UntypedFormGroup } from '@angular/forms';
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
import { FormUtilsService } from '../../../shared/form/form-utils.service';

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
    private readonly _snackBar: MatSnackBar,
    public formUtils: FormUtilsService
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
    } else {
      this.formUtils.validateFormFields(this.form);
    }
  }

  onCancel() {
    this.location.back();
  }

  onAddNewLessonClick() {
    const lessons = this.form.get('lessons') as UntypedFormArray;
    lessons.push(this.createLesson());
  }

  onDeleteLessonClick(index: number) {
    const lessons = this.form.get('lessons') as UntypedFormArray;
    lessons.removeAt(index);
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

  getMaxLength(fieldName: string): number {
    if (fieldName === 'name') {
      return this.NAME_MAX_LENGTH;
    }
    return 0;
  }

  getLessonsFormArray() {
    return (<UntypedFormArray>this.form.get('lessons')).controls;
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

  private createLesson(lesson: Lesson = {_id: '', title: '', videoCode: ''}) {
    return this.formBuilder.group({
      _id: [lesson._id],
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
