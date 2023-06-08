package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.GameDetailsDao;
import com.stadium.mystadium.dao.GameScheduleDao;
import com.stadium.mystadium.enums.CompetitionEnum;

import java.time.LocalDate;
import java.util.List;

public interface GameService {

    List<GameDetailsDao> searchGames(String name, LocalDate date, CompetitionEnum competition);

    GameScheduleDao scheduleGame(GameScheduleDao gameScheduleDao);
}
