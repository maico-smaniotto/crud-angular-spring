package com.maicosmaniotto.crud_spring.enums;

public enum Category {
    FRONTENT (1, "Front-end"), 
    BACKEND  (2, "Back-end"), 
    DATABASE (3, "Database");

    private int value;
    private String valueString;

    private Category(int value, String valueString) {
        this.value = value;
        this.valueString = valueString;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return valueString;
    }
}
