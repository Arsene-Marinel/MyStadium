package com.stadium.mystadium.mapper;

import com.stadium.mystadium.dao.PlayerDao;
import com.stadium.mystadium.dao.PlayerDetailsDao;
import com.stadium.mystadium.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerDao mapToPlayerDao(Player player) {
        return PlayerDao.builder()
                .name(player.getName())
                .numberOfTShirt(player.getNumberOfTShirt())
                .sportPlayer(player.getSportPlayer())
                .build();
    }

    public Player mapToPlayer(PlayerDao playerDao) {
        return Player.builder()
                .name(playerDao.getName())
                .sportPlayer(playerDao.getSportPlayer())
                .numberOfTShirt(playerDao.getNumberOfTShirt())
                .build();
    }

    public PlayerDetailsDao mapToPlayerDetailsDao(Player player) {
        return PlayerDetailsDao.builder()
                .id(player.getId())
                .name(player.getName())
                .sportPlayer(player.getSportPlayer())
                .numberOfTShirt(player.getNumberOfTShirt())
                .build();
    }
}
