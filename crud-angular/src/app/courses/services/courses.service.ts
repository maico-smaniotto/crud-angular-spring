import { Injectable } from '@angular/core';
import { Course } from '../model/course';
import { HttpClient } from '@angular/common/http';
import { first, tap, delay } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  // para testar erro só estragar esta url
  private readonly API = '/assets/courses.json';

  constructor(private httpClient: HttpClient) { }

  list() {
    return this.httpClient.get<Course[]>(this.API)
    .pipe(
      //take(1),
      first(), // pega só primeiro valor e cancela a inscrição
      delay(2000), // delay apenas para testar carregamento
      tap((courses: Course[]) => console.log(courses))
    );
  }
}
