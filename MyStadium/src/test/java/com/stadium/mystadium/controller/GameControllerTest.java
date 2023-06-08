package com.stadium.mystadium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadium.mystadium.dao.GameDetailsDao;
import com.stadium.mystadium.dao.GameScheduleDao;
import com.stadium.mystadium.dao.GamesFiltersDao;
import com.stadium.mystadium.dao.NewGameDao;
import com.stadium.mystadium.service.GameServiceImpl;
import com.stadium.mystadium.utils.GameMocks;
import com.stadium.mystadium.utils.GameScheduleMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

    @MockBean
    private final GameServiceImpl gameService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public GameControllerTest(GameServiceImpl gameService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.gameService = gameService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void addGameTest() throws Exception {
        NewGameDao newGameDao = GameMocks.mockNewGameDao();
        GameDetailsDao gameDetailsDao = GameMocks.mockGameDetailsDao();
        when(gameService.add(newGameDao)).thenReturn(gameDetailsDao);

        String newGameDaoBody = objectMapper.writeValueAsString(newGameDao);
        String gameDetailsDaoBody = objectMapper.writeValueAsString(gameDetailsDao);
        MvcResult result = mockMvc.perform(post("/games")
                        .content(newGameDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(gameDetailsDao.getName()))
                .andExpect(jsonPath("$.competition").value(gameDetailsDao.getCompetition().toString()))
                .andExpect(jsonPath("$.duration").value(gameDetailsDao.getDuration()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), gameDetailsDaoBody);
    }

    @Test
    public void searchGamesTest() throws Exception {
        GamesFiltersDao gamesFiltersDao = GameMocks.mockGamesFiltersDao();
        GameDetailsDao gameDetailsDao = GameMocks.mockGameDetailsDao();
        when(gameService.searchGames(gamesFiltersDao.getName(), gamesFiltersDao.getDate(), gamesFiltersDao.getCompetition())).thenReturn(List.of(gameDetailsDao));

        String gamesFiltersDaoBody = objectMapper.writeValueAsString(gamesFiltersDao);
        String gameDetailsDaoBody = objectMapper.writeValueAsString(List.of(gameDetailsDao));
        MvcResult result = mockMvc.perform(post("/games/search")
                        .content(gamesFiltersDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), gameDetailsDaoBody);
    }

    @Test
    public void scheduleGameTest() throws Exception {
        GameScheduleDao gameScheduleDao = GameScheduleMocks.mockGameScheduleDao();
        when(gameService.scheduleGame(any())).thenReturn(gameScheduleDao);

        String gameScheduleDaoBody = objectMapper.writeValueAsString(gameScheduleDao);
        MvcResult result = mockMvc.perform(post("/games/schedule")
                        .content(gameScheduleDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameName").value(gameScheduleDao.getGameName()))
                .andExpect(jsonPath("$.stadiumName").value(gameScheduleDao.getStadiumName()))
                .andExpect(jsonPath("$.date").value(gameScheduleDao.getDate().toString()))
                //.andExpect(jsonPath("$.hour").value(gameScheduleDao.getHour().toString()))
                .andExpect(jsonPath("$.price").value(gameScheduleDao.getPrice()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), gameScheduleDaoBody);
    }
}
