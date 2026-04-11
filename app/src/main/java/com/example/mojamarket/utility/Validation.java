package com.example.mojamarket.utility;
import java.util.regex.Pattern;

public class Validation {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);

    // Name and Surname Validation
    public static ValidationResult validateName(String name) {
        if (name == null || name.trim().isEmpty())
            return new ValidationResult(false, "Name required");

        if (name.length() < 2)
            return new ValidationResult(false, "Name too short");

        return new ValidationResult(true, "");
    }

    public static ValidationResult validateSurname(String surname) {
        if (surname == null || surname.trim().isEmpty())
            return new ValidationResult(false, "Surname required");

        if (surname.length() < 2)
            return new ValidationResult(false, "Surname too short");

        return new ValidationResult(true, "");
    }

    // Username
    public static ValidationResult validateUsername(String username) {
        if (username == null || username.trim().isEmpty())
            return new ValidationResult(false, "Username required");

        if (username.length() < 3)
            return new ValidationResult(false, "Username too short");

        return new ValidationResult(true, "");
    }

    // Email
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.isEmpty())
            return new ValidationResult(false, "Email required");

        if (!EMAIL_PATTERN.matcher(email).matches())
            return new ValidationResult(false, "Invalid email format");

        return new ValidationResult(true, "");
    }

    // Password
    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty())
            return new ValidationResult(false, "Password required");

        if (password.length() < 6)
            return new ValidationResult(false, "Password too short");

        return new ValidationResult(true, "");
    }
}