import { Injectable } from '@angular/core';
import { Course } from '../model/course';
import { HttpClient } from '@angular/common/http';
import { first, tap } from 'rxjs';
import { Page } from '../model/page';
// import { delay } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  // para testar erro só estragar esta url
  private readonly API = '/api/courses';

  constructor(private readonly httpClient: HttpClient) { }

  // list() {
  //   return this.httpClient.get<Course[]>(this.API)
  //   .pipe(
  //     //take(1),
  //     first(), // pega só primeiro valor e cancela a inscrição
  //     //delay(2000), // delay apenas para testar carregamento
  //     tap((courses: Course[]) => console.log(courses))
  //   );
  // }
  list(page = 0, pageSize = 10) {
    return this.httpClient.get<Page<Course>>(this.API, {params: {page, pageSize}})
    .pipe(
      //take(1),
      first(), // pega só primeiro valor e cancela a inscrição
      //delay(2000), // delay apenas para testar carregamento
      // tap((courses: Course[]) => console.log(courses))
    );
  }

  loadById(id: string) {
    return this.httpClient.get<Course>(`${this.API}/${id}`);
  }

  save(obj: Partial<Course>) {
    if (obj._id) {
      return this.update(obj);
    }
    return this.create(obj);
  }

  private create(obj: Partial<Course>) {
    return this.httpClient.post<Course>(this.API, obj);
  }

  private update(obj: Partial<Course>) {
    return this.httpClient.put<Course>(`${this.API}/${obj._id}`, obj);
  }

  delete(id: string) {
    return this.httpClient.delete<Course>(`${this.API}/${id}`);
  }

}
