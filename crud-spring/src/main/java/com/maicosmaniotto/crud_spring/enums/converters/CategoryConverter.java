package com.maicosmaniotto.crud_spring.enums.converters;

import com.maicosmaniotto.crud_spring.enums.Category;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category, Integer> {
    
    @Override
    public Integer convertToDatabaseColumn(Category attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public Category convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }
        return Stream.of(Category.values())        
            .filter(c -> c.getValue() == value)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static Category stringToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(Category.values())        
            .filter(c -> c.toString().equals(value))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
