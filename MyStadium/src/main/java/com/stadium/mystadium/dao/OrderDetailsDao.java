package com.stadium.mystadium.dao;

import com.stadium.mystadium.validator.OnlyLettersAndDigits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDao {

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

    @NotNull(message = "Purchase date must be defined!")
    private LocalDate purchaseDate;

    @NotNull(message = "Purchase time must be defined!")
    private LocalTime purchaseTime;

    @NotNull(message = "Total price must be provided!")
    @Min(value = 1, message = "Price can't be negative!")
    private Integer totalPrice;

    @NotNull(message = "Tickets must be defined!")
    @Size(min = 1, message = "There should be at least one ticket!")
    private List<TicketDao> tickets;
}
