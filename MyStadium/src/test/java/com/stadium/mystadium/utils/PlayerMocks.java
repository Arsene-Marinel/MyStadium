package com.stadium.mystadium.utils;

import com.stadium.mystadium.dao.PlayerDao;
import com.stadium.mystadium.dao.PlayerDetailsDao;
import com.stadium.mystadium.entity.Player;
import com.stadium.mystadium.enums.SportEnum;

import java.util.ArrayList;

public class PlayerMocks {

    public static PlayerDao mockPlayerDao() {
        return PlayerDao.builder()
                .name("Test player")
                .sportPlayer(SportEnum.FOOTBALL)
                .numberOfTShirt(10)
                .build();
    }

    public static PlayerDetailsDao mockPlayerDetailsDao(Long id) {
        return PlayerDetailsDao.builder()
                .id(id)
                .name("Test" + " player".repeat(id.intValue()))
                .sportPlayer(SportEnum.FOOTBALL)
                .numberOfTShirt(10)
                .build();
    }

    public static PlayerDetailsDao mockPlayerDetailsDao() {
        return PlayerDetailsDao.builder()
                .id(1L)
                .name("Test player")
                .sportPlayer(SportEnum.FOOTBALL)
                .numberOfTShirt(10)
                .build();
    }

    public static Player mockPlayer() {
        return Player.builder()
                .id(1L)
                .name("Test player")
                .games(new ArrayList<>())
                .build();
    }
}
