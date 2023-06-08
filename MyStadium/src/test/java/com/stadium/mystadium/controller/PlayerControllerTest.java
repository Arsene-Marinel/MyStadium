package com.stadium.mystadium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadium.mystadium.dao.PlayerDao;
import com.stadium.mystadium.dao.PlayerDetailsDao;
import com.stadium.mystadium.enums.SportEnum;
import com.stadium.mystadium.service.PlayerServiceImpl;
import com.stadium.mystadium.utils.PlayerMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @MockBean
    private final PlayerServiceImpl playerService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public PlayerControllerTest(PlayerServiceImpl playerService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.playerService = playerService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getActorsTest() throws Exception {
        List<PlayerDetailsDao> players = new ArrayList<>();
        for(long i = 1; i <= 5; i++) {
            players.add(PlayerMocks.mockPlayerDetailsDao(i));
        }
        when(playerService.getPlayers()).thenReturn(players);

        String playersBody = objectMapper.writeValueAsString(players);
        MvcResult result = mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), playersBody);
    }

    @Test
    public void addPlayerTest() throws Exception {
        PlayerDao playerDao = PlayerMocks.mockPlayerDao();
        when(playerService.add(any())).thenReturn(playerDao);

        String playerDaoBody = objectMapper.writeValueAsString(playerDao);
        MvcResult result = mockMvc.perform(post("/players")
                        .content(playerDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(playerDao.getName()))
                //.andExpect(jsonPath("$.sportPlayer").value(SportEnum.FOOTBALL))
                .andExpect(jsonPath("$.numberOfTShirt").value(10))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), playerDaoBody);
    }

    @Test
    public void editPlayerTest() throws Exception {
        PlayerDao playerDao = PlayerMocks.mockPlayerDao();
        when(playerService.add(any())).thenReturn(playerDao);

        String playerDaoBody = objectMapper.writeValueAsString(playerDao);
        MvcResult result = mockMvc.perform(put("/players/1")
                        .content(playerDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.name").value("Test player 1"))
                //.andExpect(jsonPath("$.sportPlayer").value(SportEnum.HANDBALL))
                //.andExpect(jsonPath("$.numberOfTShirt").value(11))
                .andReturn();
        //assertEquals(result.getResponse().getContentAsString(), playerDaoBody);
    }

    @Test
    public void deleteActorTest() throws Exception {
        PlayerDao playerDao = PlayerMocks.mockPlayerDao();
        when(playerService.add(any())).thenReturn(playerDao);

        String playerDaoBody = objectMapper.writeValueAsString(playerDao);
        MvcResult result = mockMvc.perform(delete("/players/1"))
                .andExpect(status().isOk())
                .andReturn();
        //assertEquals(result.getResponse().getContentAsString(), playerDaoBody);
    }

}
