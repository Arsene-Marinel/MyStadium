package com.stadium.mystadium.service;

import com.stadium.mystadium.composed_id.GameScheduleId;
import com.stadium.mystadium.dao.GameDao;
import com.stadium.mystadium.dao.GameDetailsDao;
import com.stadium.mystadium.dao.GameScheduleDao;
import com.stadium.mystadium.dao.NewGameDao;
import com.stadium.mystadium.entity.Game;
import com.stadium.mystadium.entity.GameSchedule;
import com.stadium.mystadium.entity.Player;
import com.stadium.mystadium.entity.Stadium;
import com.stadium.mystadium.enums.CompetitionEnum;
import com.stadium.mystadium.exception.BadRequestException;
import com.stadium.mystadium.exception.UniqueConstraintException;
import com.stadium.mystadium.mapper.GameMapper;
import com.stadium.mystadium.mapper.GameScheduleMapper;
import com.stadium.mystadium.repository.GameRepository;
import com.stadium.mystadium.repository.GameScheduleRepository;
import com.stadium.mystadium.repository.PlayerRepository;
import com.stadium.mystadium.repository.StadiumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stadium.mystadium.utils.Constants.*;

@Service
public class GameServiceImpl implements GameService, CommonService<GameDetailsDao, NewGameDao> {

    private final GameRepository gameRepository;

    private final PlayerRepository playerRepository;

    private final StadiumRepository stadiumRepository;

    private final GameScheduleRepository gameScheduleRepository;

    private final GameMapper gameMapper;

    private final GameScheduleMapper gameScheduleMapper;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, PlayerRepository playerRepository, StadiumRepository stadiumRepository, GameScheduleRepository gameScheduleRepository, GameMapper gameMapper, GameScheduleMapper gameScheduleMapper) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.stadiumRepository = stadiumRepository;
        this.gameScheduleRepository = gameScheduleRepository;
        this.gameMapper = gameMapper;
        this.gameScheduleMapper = gameScheduleMapper;
    }

    @Override
    public GameDetailsDao add(NewGameDao newGameDao) {
        List<Player> players = new ArrayList<>();
        for(Long playerId : newGameDao.getPlayerIds()) {
            Optional<Player> optionalPlayer = playerRepository.findById(playerId);
            if(optionalPlayer.isEmpty()) {
                throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "player", "id", playerId));
            }
            players.add(optionalPlayer.get());
        }
        GameDao gameDao = gameMapper.mapToGameDao(newGameDao, players);
        Game game = gameRepository.save(gameMapper.mapToGame(gameDao));
        game.setGameSchedules(game.getGameSchedules());

        return gameMapper.mapToGameDetailsDao(game);
    }

    @Override
    public List<GameDetailsDao> searchGames(String name, LocalDate date, CompetitionEnum competition) {
        List<GameDetailsDao> games = gameRepository.findAll().stream().map(gameMapper::mapToGameDetailsDao).toList();

        if(name != null) {
            games = games.stream().filter(game -> name.equals(game.getName())).toList();
        }
        if(date != null) {
            for(GameDetailsDao game : games) {
                GameDetailsDao.ScheduleDetails scheduleDetails = game.getScheduleDetails();
                if(scheduleDetails.getDate() == null) {
                    game.setScheduleDetails(scheduleDetails);
                }
            }
        }
        if(competition != null) {
            games = games.stream().filter(game -> competition.equals(game.getCompetition())).toList();
        }

        return games;
    }

    @Override
    public GameScheduleDao scheduleGame(GameScheduleDao gameScheduleDao) {
        String gameName = gameScheduleDao.getGameName();
        Optional<Game> optionalGame = gameRepository.findByName(gameName);
        if(optionalGame.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "game", "name", gameName));
        }

        String stadiumName = gameScheduleDao.getStadiumName();
        Optional<Stadium> optionalStadium = stadiumRepository.findByName(stadiumName);
        if(optionalStadium.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "stadium", "name", stadiumName));
        }

        Game game = optionalGame.get();
        LocalDate date = gameScheduleDao.getDate();
        LocalTime hour = gameScheduleDao.getHour();
        LocalDateTime scheduleStartTime = LocalDateTime.of(date, hour);
        LocalDateTime scheduleEndTime = scheduleStartTime.plusMinutes(game.getDuration());

        if(scheduleStartTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException(PAST_DATE_SCHEDULE);
        }

        Stadium stadium = optionalStadium.get();
        for(GameSchedule gameSchedule : stadium.getSchedules()) {
            LocalDateTime startTime = LocalDateTime.of(gameSchedule.getId().getDate(), gameSchedule.getId().getHour());
            LocalDateTime endTime = startTime.plusMinutes(gameSchedule.getGame().getDuration());
            if(scheduleStartTime.isBefore(endTime) && startTime.isBefore(scheduleEndTime)) {
                throw new BadRequestException(UNAVAILABLE_STADIUM);
            }
        }

        GameScheduleId gameScheduleId = GameScheduleId.builder()
                .gameId(game.getId())
                .stadiumId(stadium.getId())
                .date(date)
                .hour(hour)
                .build();
        GameSchedule gameSchedule = GameSchedule.builder()
                .id(gameScheduleId)
                .price(gameScheduleDao.getPrice())
                .game(game)
                .stadium(stadium)
                .tickets(new ArrayList<>())
                .build();
        return gameScheduleMapper.mapToGameScheduleDao(gameScheduleRepository.save(gameSchedule));
    }
}
