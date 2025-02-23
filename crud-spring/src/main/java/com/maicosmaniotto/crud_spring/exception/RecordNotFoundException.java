package com.maicosmaniotto.crud_spring.exception;

public class RecordNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(Long id) {
        super("Record not found with id: " + id);
    }

}
