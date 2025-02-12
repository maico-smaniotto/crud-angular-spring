import { NgModule } from '@angular/core';
import { ErrorDialogComponent } from './components/error-dialog/error-dialog.component';
import { CategoryPipe } from './pipes/category.pipe';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [],
  imports: [
    ErrorDialogComponent,
    CategoryPipe
  ],
  exports: [
    ErrorDialogComponent,
    CategoryPipe,
    ReactiveFormsModule
  ]
})
export class SharedModule { }
