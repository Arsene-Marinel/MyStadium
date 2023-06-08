package com.stadium.mystadium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadium.mystadium.dao.StadiumDao;
import com.stadium.mystadium.dao.StadiumDetailsDao;
import com.stadium.mystadium.service.StadiumServiceImpl;
import com.stadium.mystadium.utils.StadiumMocks;
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

@WebMvcTest(controllers = StadiumController.class)
public class StadiumControllerTest {

    @MockBean
    private final StadiumServiceImpl stadiumService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public StadiumControllerTest(StadiumServiceImpl stadiumService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.stadiumService = stadiumService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getStadiumsTest() throws Exception {
        List<StadiumDetailsDao> stadiums = new ArrayList<>();
        for(long i = 1; i <= 5; i++) {
            stadiums.add(StadiumMocks.mockStadiumDetailsDao(i));
        }
        when(stadiumService.getStadiums()).thenReturn(stadiums);

        String stadiumsBody = objectMapper.writeValueAsString(stadiums);
        MvcResult result = mockMvc.perform(get("/stadiums"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), stadiumsBody);
    }

    @Test
    public void addStadiumTest() throws Exception {
        StadiumDao stadiumDao = StadiumMocks.mockStadiumDao();
        when(stadiumService.add(any())).thenReturn(stadiumDao);

        String stadiumDaoBody = objectMapper.writeValueAsString(stadiumDao);
        MvcResult result = mockMvc.perform(post("/stadiums")
                        .content(stadiumDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(stadiumDao.getName()))
                .andExpect(jsonPath("$.numberOfSections").value(4))
                .andExpect(jsonPath("$.rowsOfSection").value(12))
                .andExpect(jsonPath("$.seatsOfRow").value(20))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), stadiumDaoBody);
    }

    @Test
    public void editStadiumTest() throws Exception {
        StadiumDao stadiumDao = StadiumMocks.mockStadiumDao();
        when(stadiumService.editStadium(any(), any())).thenReturn(stadiumDao);

        String stadiumDaoBody = objectMapper.writeValueAsString(stadiumDao);
        MvcResult result = mockMvc.perform(put("/stadiums/1")
                        .content(stadiumDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(stadiumDao.getName()))
                .andExpect(jsonPath("$.numberOfSections").value(4))
                .andExpect(jsonPath("$.rowsOfSection").value(12))
                .andExpect(jsonPath("$.seatsOfRow").value(20))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), stadiumDaoBody);
    }

}
