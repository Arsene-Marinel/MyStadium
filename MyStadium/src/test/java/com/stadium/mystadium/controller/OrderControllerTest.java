package com.stadium.mystadium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadium.mystadium.dao.OrderDao;
import com.stadium.mystadium.dao.OrderDetailsDao;
import com.stadium.mystadium.service.OrderServiceImpl;
import com.stadium.mystadium.utils.OrderMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

    @MockBean
    private final OrderServiceImpl orderService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public OrderControllerTest(OrderServiceImpl orderService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void addOrderTest() throws Exception {
        OrderDao orderDao = OrderMocks.mockOrderDao();
        OrderDetailsDao orderDetailsDao = OrderMocks.mockOrderDetailsDao();
        when(orderService.add(any())).thenReturn(orderDetailsDao);

        String orderDaoBody = objectMapper.writeValueAsString(orderDao);
        String orderDetailsDaoBody = objectMapper.writeValueAsString(orderDetailsDao);
        MvcResult result = mockMvc.perform(post("/orders")
                        .content(orderDaoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameName").value(orderDetailsDao.getGameName()))
                .andExpect(jsonPath("$.stadiumName").value(orderDetailsDao.getStadiumName()))
                .andExpect(jsonPath("$.gameDate").value(orderDetailsDao.getGameDate().toString()))
                .andExpect(jsonPath("$.purchaseDate").value(orderDetailsDao.getPurchaseDate().toString()))
                .andExpect(jsonPath("$.totalPrice").value(orderDetailsDao.getTotalPrice()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), orderDetailsDaoBody);
    }
}
