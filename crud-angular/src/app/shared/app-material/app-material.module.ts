import { NgModule } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  exports: [
    MatToolbarModule,
    MatTableModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatDialogModule
  ],
})
export class AppMaterialModule { }
