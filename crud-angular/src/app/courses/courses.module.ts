import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoursesRoutingModule } from './courses-routing.module';
import { CoursesComponent } from './containers/courses/courses.component';
import { CourseFormComponent } from './containers/course-form/course-form.component';
import { CoursesListComponent } from './components/courses-list/courses-list.component';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { CustomPaginatorIntl } from '../shared/custom-paginator-intl';

@NgModule({
  imports: [
    CommonModule,
    CoursesRoutingModule,
    CoursesComponent,
    CourseFormComponent,
    CoursesListComponent
  ],
  providers: [
    { provide: MatPaginatorIntl, useClass: CustomPaginatorIntl }
  ]
})
export class CoursesModule { }
