import { Injectable } from '@angular/core';
import { FormArray, FormGroup, UntypedFormArray, UntypedFormControl, UntypedFormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class FormUtilsService {

  constructor() { }

  getErrorMessageFromField(field: UntypedFormControl) {
    if (field?.hasError('required')) {
      return 'Campo obrigatório';
    }
    if (field?.hasError('minlength')) {
      const minLength = field.errors ? field?.errors['minlength']['requiredLength'] : 0;
      return `Campo deve ter no mínimo ${minLength} caracteres`;
    }
    if (field?.hasError('maxlength')) {
      const maxLength = field.errors ? field?.errors['maxlength']['requiredLength'] : 0;
      return `Campo pode ter no máximo ${maxLength} caracteres`;
    }
    return 'Campo inválido';
  }

  getErrorMessageFromFormGroupField(formGroup: UntypedFormGroup, fieldName: string) {
    const field = formGroup.get(fieldName) as UntypedFormControl;
    return this.getErrorMessageFromField(field);
  }

  getErrorMessageFromFormArrayField(formGroup: UntypedFormGroup, formArrayName: string, index: number, fieldName: string) {
    const formArray = formGroup.get(formArrayName) as UntypedFormArray;
    const field = formArray.controls[index].get(fieldName) as UntypedFormControl;
    return this.getErrorMessageFromField(field);
  }

  isFormArrayRequired(formGroup: UntypedFormGroup, formArrayName: string) {
    const formArray = formGroup.get(formArrayName) as UntypedFormArray;
    return !formArray.valid && formArray.hasError('required') && formArray.touched;
  }

  validateFormFields(formGroup: UntypedFormGroup | UntypedFormArray) {
    Object.keys(formGroup.controls).forEach(
      fieldName => {
        const control = formGroup.get(fieldName);
        if (control instanceof UntypedFormControl) {
          control?.markAsTouched({ onlySelf: true });
        } else if (control instanceof FormGroup || control instanceof FormArray) {
          control?.markAsTouched({ onlySelf: true });
          this.validateFormFields(control);
        }
      }
    )
  }
}
