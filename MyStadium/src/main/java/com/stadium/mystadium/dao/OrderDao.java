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
public class OrderDao {

    @NotBlank(message = "The email must be defined!")
    @Email(message = "The email should have an valid format!")
    private String fanEmail;

    @NotNull(message = "The number of tickets must be defined!")
    @Min(value = 1, message = "The number of tickets must be at least 1!")
    private Integer numberOfTickets;

    @NotBlank(message = "Game name can't be blank!")
    private String gameName;

    @NotBlank(message = "Stadium name must contain at least one letter or digit!")
    @OnlyLettersAndDigits
    private String stadiumName;

    @NotNull(message = "The date must be defined!")
    @FutureOrPresent(message = "You can't select a past date")
    private LocalDate gameDate;

    @NotNull(message = "The hour must be defined!")
    private LocalTime gameHour;
}
