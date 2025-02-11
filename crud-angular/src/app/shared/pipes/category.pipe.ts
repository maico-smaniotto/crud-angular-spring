import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'category'
})
export class CategoryPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    switch(value) {
      case 'Frontend': return 'computer';
      case 'Backend': return 'code';
      case 'Database': return 'folder';
    }
    return 'code';
  }

}
