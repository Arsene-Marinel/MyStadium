package com.stadium.mystadium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadium.mystadium.dao.FanDao;
import com.stadium.mystadium.service.FanServiceImpl;
import com.stadium.mystadium.utils.FanMocks;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FanController.class)
public class FanControllerTest {

    @MockBean
    private final FanServiceImpl fanService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public FanControllerTest(FanServiceImpl fanService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.fanService = fanService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void addFanTest() throws Exception {
        FanDao fanDao = FanMocks.mockFanDao();
        when(fanService.add(any())).thenReturn(fanDao);

        String fanDaoBody = objectMapper.writeValueAsString(fanDao);
        MvcResult result = mockMvc.perform(post("/fans")
                        .content(fanDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(fanDao.getName()))
                .andExpect(jsonPath("$.email").value(fanDao.getEmail()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), fanDaoBody);
    }

    @Test
    public void searchFansTest() throws Exception {
        FanDao fanDao = FanMocks.mockFanDao();
        when(fanService.searchFans(any())).thenReturn(List.of(fanDao));

        String customerDtoBody = objectMapper.writeValueAsString(List.of(fanDao));
        MvcResult result = mockMvc.perform(get("/fans").requestAttr("searchParam", "test"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), customerDtoBody);
    }
}
