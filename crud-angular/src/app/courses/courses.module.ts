import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoursesRoutingModule } from './courses-routing.module';
import { AppMaterialModule } from '../shared/app-material/app-material.module';
import { CoursesComponent } from './courses/courses.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    CoursesRoutingModule,
    AppMaterialModule,
    SharedModule,
    CoursesComponent
  ],
  exports: [CoursesComponent]
})
export class CoursesModule { }
