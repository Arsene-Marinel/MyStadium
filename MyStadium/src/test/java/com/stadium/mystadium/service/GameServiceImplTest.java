package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.GameDao;
import com.stadium.mystadium.dao.GameDetailsDao;
import com.stadium.mystadium.dao.GameScheduleDao;
import com.stadium.mystadium.dao.NewGameDao;
import com.stadium.mystadium.entity.Game;
import com.stadium.mystadium.entity.GameSchedule;
import com.stadium.mystadium.entity.Player;
import com.stadium.mystadium.entity.Stadium;
import com.stadium.mystadium.exception.BadRequestException;
import com.stadium.mystadium.exception.UniqueConstraintException;
import com.stadium.mystadium.mapper.GameMapper;
import com.stadium.mystadium.mapper.GameScheduleMapper;
import com.stadium.mystadium.repository.GameRepository;
import com.stadium.mystadium.repository.GameScheduleRepository;
import com.stadium.mystadium.repository.PlayerRepository;
import com.stadium.mystadium.repository.StadiumRepository;
import com.stadium.mystadium.utils.GameMocks;
import com.stadium.mystadium.utils.GameScheduleMocks;
import com.stadium.mystadium.utils.PlayerMocks;
import com.stadium.mystadium.utils.StadiumMocks;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.stadium.mystadium.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private StadiumRepository roomRepository;

    @Mock
    private GameScheduleRepository gameScheduleRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private GameScheduleMapper gameScheduleMapper;

    @Test
    public void addGameTest() {
        Game game = GameMocks.mockGame();
        NewGameDao newGameDao = GameMocks.mockNewGameDao();
        Player player = PlayerMocks.mockPlayer();
        GameDao gameDao = GameMocks.mockgameDao(List.of(player));
        GameDetailsDao gameDetailsDao = GameMocks.mockGameDetailsDao();

        when(gameRepository.save(game)).thenReturn(game);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(gameMapper.mapToGameDao(newGameDao, List.of(player))).thenReturn(gameDao);
        when(gameMapper.mapToGame(gameDao)).thenReturn(game);
        when(gameMapper.mapToGameDetailsDao(game)).thenReturn(gameDetailsDao);
        GameDetailsDao result = gameService.add(newGameDao);

        assertEquals(result.getName(), gameDetailsDao.getName());
        assertEquals(result.getDuration(), gameDetailsDao.getDuration());
        assertEquals(result.getCompetition(), gameDetailsDao.getCompetition());
    }

    @Test
    public void addGameThrowsEntityNotFoundExceptionTest() {
        NewGameDao newGameDao = GameMocks.mockNewGameDao();

        when(playerRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> gameService.add(newGameDao));
        assertEquals(String.format(ENTITY_NOT_FOUND, "player", "id", 1), entityNotFoundException.getMessage());
    }

    @Test
    public void searchGamesTest() {
        Game game = GameMocks.mockGame();
        GameDetailsDao gameDetailsDao = GameMocks.mockGameDetailsDao();

        when(gameRepository.findAll()).thenReturn(List.of(game));
        when(gameMapper.mapToGameDetailsDao(game)).thenReturn(gameDetailsDao);
        List<GameDetailsDao> result = gameService.searchGames(game.getName(), LocalDate.now().plusDays(3), game.getCompetition());

        assertEquals(result, List.of(gameDetailsDao));
    }

    @Test
    public void scheduleGameThrowsGameEntityNotFoundExceptionTest() {
        GameScheduleDao gameScheduleDao = GameScheduleMocks.mockGameScheduleDao();

        when(gameRepository.findByName("Test game")).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> gameService.scheduleGame(gameScheduleDao));
        assertEquals(String.format(ENTITY_NOT_FOUND, "game", "name", "Test game"), entityNotFoundException.getMessage());
    }

    @Test
    public void scheduleGameThrowsStadiumEntityNotFoundExceptionTest() {
        GameScheduleDao gameScheduleDao = GameScheduleMocks.mockGameScheduleDao();
        Game game = GameMocks.mockGame();

        when(gameRepository.findByName("Test game")).thenReturn(Optional.of(game));
        when(roomRepository.findByName("Test stadium")).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> gameService.scheduleGame(gameScheduleDao));
        assertEquals(String.format(ENTITY_NOT_FOUND, "stadium", "name", "Test stadium"), entityNotFoundException.getMessage());
    }

    @Test
    public void scheduleGameThrowsPastDateBadRequestExceptionTest() {
        GameScheduleDao gameScheduleDao = GameScheduleMocks.mockGameScheduleDao();
        gameScheduleDao.setDate(LocalDate.now().minusDays(3));
        Game game = GameMocks.mockGame();
        Stadium stadium = StadiumMocks.mockStadium();

        when(gameRepository.findByName("Test game")).thenReturn(Optional.of(game));
        when(roomRepository.findByName("Test stadium")).thenReturn(Optional.of(stadium));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> gameService.scheduleGame(gameScheduleDao));
        assertEquals(PAST_DATE_SCHEDULE, badRequestException.getMessage());
    }

    @Test
    public void scheduleGameThrowsStadiumNotAvailableBadRequestExceptionTest() {
        GameScheduleDao gameScheduleDao = GameScheduleMocks.mockGameScheduleDao();
        Game game = GameMocks.mockGame();
        Stadium stadium = StadiumMocks.mockStadium();
        stadium.setSchedules(List.of(GameScheduleMocks.mockGameSchedule(game, stadium)));

        when(gameRepository.findByName("Test game")).thenReturn(Optional.of(game));
        when(roomRepository.findByName("Test stadium")).thenReturn(Optional.of(stadium));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> gameService.scheduleGame(gameScheduleDao));
        assertEquals(UNAVAILABLE_STADIUM, badRequestException.getMessage());
    }
}
