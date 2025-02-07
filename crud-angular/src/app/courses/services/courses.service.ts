import { Injectable } from '@angular/core';
import { Course } from '../model/course';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  constructor(private httpClient: HttpClient) { }

  list(): Course[] {
    return [
      { _id: '1', name: 'Angular', category: 'Frontend' },
      { _id: '2', name: 'React', category: 'Frontend' },
      { _id: '3', name: 'Java', category: 'Backend' },
      { _id: '4', name: 'Spring', category: 'Backend' }
    ];
  }
}
