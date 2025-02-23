package com.maicosmaniotto.crud_spring.enums.validation;

import java.util.Arrays;

import com.maicosmaniotto.crud_spring.enums.Category;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryStringSubsetValidator implements ConstraintValidator<CategoryStringSubset, String> {
    private Category[] subset;

    @Override
    public void initialize(CategoryStringSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || Arrays.stream(subset).map(Object::toString).toList().contains(value);
    }
}
