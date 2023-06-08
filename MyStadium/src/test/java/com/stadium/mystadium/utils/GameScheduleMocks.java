package com.stadium.mystadium.utils;

import com.stadium.mystadium.composed_id.GameScheduleId;
import com.stadium.mystadium.dao.GameScheduleDao;
import com.stadium.mystadium.entity.Game;
import com.stadium.mystadium.entity.GameSchedule;
import com.stadium.mystadium.entity.Stadium;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GameScheduleMocks {

    public static GameScheduleDao mockGameScheduleDao() {
        return GameScheduleDao.builder()
                .gameName("Test game")
                .stadiumName("Test stadium")
                .date(LocalDate.of(2023, 6, 8))
                .hour(LocalTime.of(18, 0, 0))
                .price(10)
                .build();
    }

    public static GameSchedule mockGameSchedule(Game game, Stadium stadium) {
        return GameSchedule.builder()
                .id(mockGameScheduleId())
                .game(game)
                .stadium(stadium)
                .price(18)
                .tickets(new ArrayList<>())
                .build();
    }

    public static GameScheduleId mockGameScheduleId() {
        return GameScheduleId.builder()
                .gameId(1L)
                .stadiumId(1L)
                .date(LocalDate.of(2023, 6, 8))
                .hour(LocalTime.of(18, 0, 0))
                .build();
    }
}
