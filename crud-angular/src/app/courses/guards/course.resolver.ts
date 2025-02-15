import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { CoursesService } from '../services/courses.service';
import { of } from 'rxjs';
import { Course } from '../model/course';

export const courseResolver: ResolveFn<Course> = (route, state) => {
  if (route.params && route.params['id']) {
    return inject(CoursesService).loadById(route.params['id']);
  }
  return of({_id: '', name: '', category: ''});
};
