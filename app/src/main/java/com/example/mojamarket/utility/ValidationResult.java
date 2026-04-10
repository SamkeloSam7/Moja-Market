package com.example.mojamarket.utility;

public class ValidationResult {
    public boolean isValid;
    public String message;

    public ValidationResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }
}