package com.stadium.mystadium.dao;

import com.stadium.mystadium.validator.OnlyLettersAndDigits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameScheduleDao {

    @NotBlank(message = "Game name can't be null!")
    private String gameName;

    @NotBlank(message = "Stadium name can't be null!")
    @OnlyLettersAndDigits
    private String stadiumName;

    @NotNull(message = "Schedule date can't be null!")
    @FutureOrPresent(message = "Schedule date can't be a past date!")
    private LocalDate date;

    @NotNull(message = "Schedule hour can't be null!")
    private LocalTime hour;

    @NotNull(message = "Game price must be defined!")
    @Min(value = 1, message = "Game price must be a positive number!")
    private Integer price;
}
