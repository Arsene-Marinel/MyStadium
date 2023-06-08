package com.stadium.mystadium.dao;

import com.stadium.mystadium.enums.CompetitionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewGameDao {

    @NotBlank(message = "Game name can't be blank!")
    private String name;

    @NotBlank(message = "Competition can't be undefined!")
    private CompetitionEnum competition;

    @NotNull(message = "Game duration must be defined!")
    @Min(value = 1, message = "Game duration must be a positive number!")
    private Integer duration;

    @NotNull(message = "Players must be defined!")
    @Size(min = 1, message = "Players list can't be empty!")
    private List<Long> playerIds;
}
