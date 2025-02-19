package com.maicosmaniotto.crud_spring.enums;

public enum RecordStatus {
    ACTIVE   ('A', "Active"),
    INACTIVE ('I', "Inactive"),
    DELETED  ('X', "Deleted");

    private final char value;
    private final String valueString;

    private RecordStatus(char value, String valueString) {
        this.value = value;
        this.valueString = valueString;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return valueString;
    }
}
