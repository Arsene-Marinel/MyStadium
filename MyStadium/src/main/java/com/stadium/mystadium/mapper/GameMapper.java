package com.stadium.mystadium.mapper;

import com.stadium.mystadium.dao.GameDao;
import com.stadium.mystadium.dao.GameDetailsDao;
import com.stadium.mystadium.dao.NewGameDao;
import com.stadium.mystadium.entity.Game;
import com.stadium.mystadium.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameMapper {

    private final PlayerMapper playerMapper;

    @Autowired
    public GameMapper(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    public GameDao mapToGameDao(NewGameDao game, List<Player> players) {
        return GameDao.builder()
                .name(game.getName())
                .competition(game.getCompetition())
                .duration(game.getDuration())
                .players(players)
                .build();
    }

    public Game mapToGame(GameDao gameDao) {
        return Game.builder()
                .name(gameDao.getName())
                .competition(gameDao.getCompetition())
                .duration(gameDao.getDuration())
                .players(gameDao.getPlayers())
                .build();
    }

    public GameDetailsDao mapToGameDetailsDao(Game game) {
        return GameDetailsDao.builder()
                .name(game.getName())
                .competition(game.getCompetition())
                .duration(game.getDuration())
                .players(game.getPlayers().stream().map(playerMapper::mapToPlayerDao).toList())
                .scheduleDetails(
                                GameDetailsDao.ScheduleDetails.builder()
                                        .date(game.getGameSchedules().getId().getDate())
                                        .hour(game.getGameSchedules().getId().getHour())
                                        .price(game.getGameSchedules().getPrice())
                                        .stadiumName(game.getGameSchedules().getStadium().getName())
                                        .build()
                )
                .build();
    }
}
