package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.PlayerDao;
import com.stadium.mystadium.dao.PlayerDetailsDao;
import com.stadium.mystadium.entity.Game;
import com.stadium.mystadium.entity.Player;
import com.stadium.mystadium.exception.BadRequestException;
import com.stadium.mystadium.mapper.PlayerMapper;
import com.stadium.mystadium.repository.PlayerRepository;
import com.stadium.mystadium.utils.GameMocks;
import com.stadium.mystadium.utils.PlayerMocks;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.stadium.mystadium.utils.Constants.ENTITY_NOT_FOUND;
import static com.stadium.mystadium.utils.Constants.PLAYER_IS_IN_TEAM_GAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Test
    public void getPlayersTest() {
        PlayerDetailsDao playerDetailsDao = PlayerMocks.mockPlayerDetailsDao();
        List<PlayerDetailsDao> playerDetailsDaoList = List.of(playerDetailsDao);
        Player player = PlayerMocks.mockPlayer();
        List<Player> players = List.of(player);

        when(playerRepository.findAll()).thenReturn(players);
        when(playerMapper.mapToPlayerDetailsDao(player)).thenReturn(playerDetailsDao);

        List<PlayerDetailsDao> result = playerService.getPlayers();
        assertEquals(result, playerDetailsDaoList);
    }

    @Test
    public void addPlayerTest() {
        Player player = PlayerMocks.mockPlayer();
        PlayerDao playerDao = PlayerMocks.mockPlayerDao();

        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.mapToPlayer(playerDao)).thenReturn(player);
        when(playerMapper.mapToPlayerDao(player)).thenReturn(playerDao);
        PlayerDao result = playerService.add(playerDao);

        assertEquals(result.getName(), playerDao.getName());
    }

    @Test
    public void editPlayerTest() {
        Player player = PlayerMocks.mockPlayer();
        PlayerDao playerDao = PlayerMocks.mockPlayerDao();
        player.setName("Test player edit");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        playerDao.setName("Test player edit");
        when(playerMapper.mapToPlayerDao(player)).thenReturn(playerDao);
        when(playerRepository.save(player)).thenReturn(player);
        PlayerDao result = playerService.editPlayer(1L, playerDao);

        assertEquals(result.getName(), playerDao.getName());
        assertEquals(result.getName(), "Test player edit");
    }

    @Test
    public void deletePlayerTest() {
        Player player = PlayerMocks.mockPlayer();
        PlayerDao playerDao = PlayerMocks.mockPlayerDao();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerMapper.mapToPlayerDao(player)).thenReturn(playerDao);
        PlayerDao result = playerService.deletePlayer(1L);

        assertEquals(result.getName(), playerDao.getName());
        assertEquals(result.getName(), "Test player");
    }

    @Test
    public void deletePlayerThrowsEntityNotFoundExceptionTest() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> playerService.deletePlayer(1L));
        assertEquals(String.format(ENTITY_NOT_FOUND, "player", "id", 1), entityNotFoundException.getMessage());
    }

    @Test
    public void deletePlayerThrowsBadRequestExceptionTest() {
        Player player = PlayerMocks.mockPlayer();
        Game game = GameMocks.mockGame();
        player.getGames().add(game);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> playerService.deletePlayer(1L));
        assertEquals(PLAYER_IS_IN_TEAM_GAME, badRequestException.getMessage());
    }
}
