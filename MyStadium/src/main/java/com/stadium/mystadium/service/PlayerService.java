package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.PlayerDao;
import com.stadium.mystadium.dao.PlayerDetailsDao;

import java.util.List;

public interface PlayerService {

    List<PlayerDetailsDao> getPlayers();

    PlayerDao editPlayer(Long id, PlayerDao playerDao);

    PlayerDao deletePlayer(Long id);
}
