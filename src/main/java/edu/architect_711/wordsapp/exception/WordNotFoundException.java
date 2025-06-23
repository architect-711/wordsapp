package edu.architect_711.wordsapp.exception;

import jakarta.persistence.EntityNotFoundException;

public class WordNotFoundException extends EntityNotFoundException {
    public WordNotFoundException(String message) {
        super(message);
    }
}
