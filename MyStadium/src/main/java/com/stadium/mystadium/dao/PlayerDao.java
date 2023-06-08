package com.stadium.mystadium.dao;

import com.stadium.mystadium.enums.CompetitionEnum;
import com.stadium.mystadium.enums.SportEnum;
import com.stadium.mystadium.validator.OnlyDigits;
import com.stadium.mystadium.validator.OnlyLetters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDao {

    @NotBlank(message = "Player name must contain at least one letter!")
    @OnlyLetters
    private String name;

    @NotNull(message = "Game duration must be defined!")
    @OnlyDigits
    private Integer numberOfTShirt;

    @NotBlank(message = "Sport can't be undefined!")
    private SportEnum sportPlayer;
}
