import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Course } from '../../model/course';

import { CategoryPipe } from '../../../shared/pipes/category.pipe';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrl: './courses-list.component.scss',
  standalone: true,
  imports: [
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
    CategoryPipe
]
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
