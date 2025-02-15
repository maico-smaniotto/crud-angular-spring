import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { NgModule } from '@angular/core';
import { ErrorDialogComponent } from './components/error-dialog/error-dialog.component';
import { CategoryPipe } from './pipes/category.pipe';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [],
  imports: [
    ErrorDialogComponent,
    ConfirmDialogComponent,
    CategoryPipe
  ],
  exports: [
    ErrorDialogComponent,
    ConfirmDialogComponent,
    CategoryPipe,
    ReactiveFormsModule
  ]
})
export class SharedModule { }
