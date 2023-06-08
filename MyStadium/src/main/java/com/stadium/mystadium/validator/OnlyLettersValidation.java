package com.stadium.mystadium.validator;

import jakarta.validation.*;

public class OnlyLettersValidation implements ConstraintValidator<OnlyLetters, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) {
            return false;
        }
        return value.matches("^[a-zA-Z -]*$");
    }
}
