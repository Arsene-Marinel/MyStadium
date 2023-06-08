package com.stadium.mystadium.dao;

import com.stadium.mystadium.validator.OnlyLettersAndDigits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StadiumDao {

    @NotBlank(message = "The stadium name must contain at least one letter or digit!")
    @OnlyLettersAndDigits
    private String name;

    @NotNull
    @Min(value = 1, message = "The stadium must have minimum 1 section!")
    private Integer numberOfSections;

    @NotNull
    @Min(value = 1, message = "Each section must have minimum 1 row!")
    private Integer rowsOfSection;

    @NotNull
    @Min(value = 1, message = "Each row must have minimum 1 seat!")
    private Integer seatsOfRow;
}
