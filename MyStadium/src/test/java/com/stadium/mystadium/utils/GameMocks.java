package com.stadium.mystadium.utils;

import com.stadium.mystadium.dao.GameDao;
import com.stadium.mystadium.dao.GameDetailsDao;
import com.stadium.mystadium.dao.GamesFiltersDao;
import com.stadium.mystadium.dao.NewGameDao;
import com.stadium.mystadium.entity.Game;
import com.stadium.mystadium.entity.GameSchedule;
import com.stadium.mystadium.entity.Player;
import com.stadium.mystadium.enums.CompetitionEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GameMocks {

    public static NewGameDao mockNewGameDao() {
        return NewGameDao.builder()
                .name("Test game")
                .competition(CompetitionEnum.CHAMPIONS_LEAGUE)
                .duration(80)
                .playerIds(List.of(1L))
                .build();
    }

    public static GameDetailsDao mockGameDetailsDao() {
        return GameDetailsDao.builder()
                .name("Test game")
                .competition(CompetitionEnum.CHAMPIONS_LEAGUE)
                .duration(90)
                .players(List.of(PlayerMocks.mockPlayerDao()))
                .scheduleDetails(GameDetailsDao.ScheduleDetails.builder()
                                .stadiumName("Test stadium")
                                .date(LocalDate.now().plusDays(3))
                                .hour(LocalTime.now())
                                .price(5)
                                .build()
                )
                .build();
    }

    public static GamesFiltersDao mockGamesFiltersDao() {
        return GamesFiltersDao.builder()
                .name("Test game")
                .date(LocalDate.of(2023, 6, 9))
                .competition(CompetitionEnum.CHAMPIONS_LEAGUE)
                .build();
    }

    public static Game mockGame() {
        return Game.builder()
                .id(1L)
                .name("Test game")
                .duration(100)
                .competition(CompetitionEnum.CHAMPIONS_LEAGUE)
                .players(new ArrayList<>())
                .gameSchedules(new GameSchedule())
                .build();
    }

    public static GameDao mockgameDao(List<Player> players) {
        return GameDao.builder()
                .name("Test game")
                .duration(100)
                .competition(CompetitionEnum.CHAMPIONS_LEAGUE)
                .players(players)
                .build();
    }
}
