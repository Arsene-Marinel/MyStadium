package com.stadium.mystadium.validator;

import jakarta.validation.*;

public class OnlyDigitsValidation implements ConstraintValidator<OnlyLettersAndDigits, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) {
            return false;
        }
        return value.matches("^[0-9 -]*$");
    }
}
