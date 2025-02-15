import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Course } from '../../model/course';
import { AppMaterialModule } from '../../../shared/app-material/app-material.module';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-courses-list',
  imports: [CommonModule, AppMaterialModule, SharedModule],
  templateUrl: './courses-list.component.html',
  styleUrl: './courses-list.component.scss'
})
export class CoursesListComponent {

  @Input() courses: Course[] = [];
  @Output() add = new EventEmitter(false);
  @Output() edit = new EventEmitter<Course>();
  @Output() remove = new EventEmitter<Course>();

  readonly displayedColumns = ['_id', 'name', 'category', 'actions'];

  constructor() { }

  onAdd() {
    this.add.emit(true)
  }

  onEdit(obj: Course) {
    this.edit.emit(obj);
  }

  onRemove(obj: Course) {
    this.remove.emit(obj);
  }
}
