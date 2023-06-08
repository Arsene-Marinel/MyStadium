package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.PlayerDao;
import com.stadium.mystadium.dao.PlayerDetailsDao;
import com.stadium.mystadium.entity.Player;
import com.stadium.mystadium.exception.BadRequestException;
import com.stadium.mystadium.exception.UniqueConstraintException;
import com.stadium.mystadium.mapper.PlayerMapper;
import com.stadium.mystadium.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stadium.mystadium.utils.Constants.*;

@Service
public class PlayerServiceImpl implements PlayerService, CommonService<PlayerDao, PlayerDao> {

    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public List<PlayerDetailsDao> getPlayers() {
        return playerRepository.findAll().stream().map(playerMapper::mapToPlayerDetailsDao).collect(Collectors.toList());
    }

    @Override
    public PlayerDao add(PlayerDao playerDao) {
        Optional<Player> optionalPlayer = playerRepository.findByName(playerDao.getName());
        if(optionalPlayer.isPresent()) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, "player", "name"));
        }
        return playerMapper.mapToPlayerDao(playerRepository.save(playerMapper.mapToPlayer(playerDao)));
    }

    @Override
    public PlayerDao editPlayer(Long id, PlayerDao playerDao) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if(optionalPlayer.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "player", "id", id));
        }

        Optional<Player> optionalPlayerByName = playerRepository.findByName(playerDao.getName());
        if(optionalPlayerByName.isPresent() && !Objects.equals(optionalPlayerByName.get().getId(), id)) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, "player", "name"));
        }

        Player player = optionalPlayer.get();
        player.setName(player.getName());
        player.setSportPlayer(player.getSportPlayer());
        player.setNumberOfTShirt(player.getNumberOfTShirt());

        return playerMapper.mapToPlayerDao(playerRepository.save(player));
    }

    @Override
    public PlayerDao deletePlayer(Long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if(optionalPlayer.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "player", "id", id));
        }

        Player player = optionalPlayer.get();

        if(!player.getGames().isEmpty()) {
            throw new BadRequestException(PLAYER_IS_IN_TEAM_GAME);
        }

        PlayerDao playerDao = playerMapper.mapToPlayerDao(player);
        playerRepository.deleteById(id);

        return playerDao;
    }
}
