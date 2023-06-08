package com.stadium.mystadium.dao;

import com.stadium.mystadium.entity.Player;
import com.stadium.mystadium.enums.CompetitionEnum;
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
public class GameDetailsDao {

    @NotBlank(message = "Game name can't be blank!")
    private String name;

    @NotBlank(message = "Competition can't be undefined!")
    private CompetitionEnum competition;

    @NotNull(message = "Game duration must be defined!")
    @Min(value = 1, message = "Game duration must be a positive number!")
    private Integer duration;

    @NotNull(message = "Players must be defined!")
    private List<PlayerDao> players;

    @NotNull(message = "Schedule details must be defined!")
    private ScheduleDetails scheduleDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDetails {
        private LocalDate date;
        private LocalTime hour;
        private Integer price;
        private String stadiumName;
    }
}