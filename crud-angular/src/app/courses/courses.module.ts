import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoursesRoutingModule } from './courses-routing.module';
import { AppMaterialModule } from '../shared/app-material/app-material.module';
import { CoursesComponent } from './containers/courses/courses.component';
import { SharedModule } from '../shared/shared.module';
import { CourseFormComponent } from './containers/course-form/course-form.component';
import { CoursesListComponent } from './components/courses-list/courses-list.component';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    CoursesRoutingModule,
    AppMaterialModule,
    SharedModule,
    CoursesComponent,
    CourseFormComponent,
    CoursesListComponent
  ],
  exports: [CoursesComponent, CourseFormComponent, CoursesListComponent]
})
export class CoursesModule { }
