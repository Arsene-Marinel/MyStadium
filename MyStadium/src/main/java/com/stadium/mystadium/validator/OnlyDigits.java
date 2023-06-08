package com.stadium.mystadium.validator;

import jakarta.validation.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {OnlyLettersValidation.class})
public @interface OnlyDigits {

    String message() default "Only digits!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
