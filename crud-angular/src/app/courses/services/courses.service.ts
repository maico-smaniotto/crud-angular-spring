import { Injectable } from '@angular/core';
import { Course } from '../model/course';
import { HttpClient } from '@angular/common/http';
import { first, tap } from 'rxjs';
// import { delay } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  // para testar erro só estragar esta url
  private readonly API = '/api/courses';

  constructor(private readonly httpClient: HttpClient) { }

  list() {
    return this.httpClient.get<Course[]>(this.API)
    .pipe(
      //take(1),
      first(), // pega só primeiro valor e cancela a inscrição
      //delay(2000), // delay apenas para testar carregamento
      tap((courses: Course[]) => console.log(courses))
    );
  }

  save(record: Course) {
    // console.log('record', record);
    return this.httpClient.post<Course>(this.API, record);
  }
}
