import { Component, Input } from '@angular/core';
import { Course } from '../model/course';
import { ActivatedRoute, Router } from '@angular/router';
import { AppMaterialModule } from '../../shared/app-material/app-material.module';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-courses-list',
  imports: [CommonModule, AppMaterialModule, SharedModule],
  templateUrl: './courses-list.component.html',
  styleUrl: './courses-list.component.scss'
})
export class CoursesListComponent {

  @Input() courses: Course[] = [];

  readonly displayedColumns = ['_id', 'name', 'category', 'actions'];

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {

  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }
}
