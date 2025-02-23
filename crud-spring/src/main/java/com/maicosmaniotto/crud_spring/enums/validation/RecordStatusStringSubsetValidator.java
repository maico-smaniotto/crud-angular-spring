package com.maicosmaniotto.crud_spring.enums.validation;

import java.util.Arrays;

import com.maicosmaniotto.crud_spring.enums.RecordStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RecordStatusStringSubsetValidator implements ConstraintValidator<RecordStatusStringSubset, String> {
    private RecordStatus[] subset;

    @Override
    public void initialize(RecordStatusStringSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || Arrays.stream(subset).map(Object::toString).toList().contains(value);
    }
}
