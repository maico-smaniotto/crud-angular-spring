package com.maicosmaniotto.crud_spring.enums.converters;

import java.util.stream.Stream;

import com.maicosmaniotto.crud_spring.enums.RecordStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RecordStatusConverter implements AttributeConverter<RecordStatus, Character> {
    
    @Override
    public Character convertToDatabaseColumn(RecordStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public RecordStatus convertToEntityAttribute(Character value) {
        if (value == null) {
            return null;
        }
        return Stream.of(RecordStatus.values())        
            .filter(c -> c.getValue() == value)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static RecordStatus stringToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(RecordStatus.values())        
            .filter(c -> c.toString().equals(value))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
