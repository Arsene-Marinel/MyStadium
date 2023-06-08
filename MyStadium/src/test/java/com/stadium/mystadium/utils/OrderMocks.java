package com.stadium.mystadium.utils;

import com.stadium.mystadium.dao.OrderDao;
import com.stadium.mystadium.dao.OrderDetailsDao;
import com.stadium.mystadium.entity.Order;
import com.stadium.mystadium.entity.Ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OrderMocks {

    public static OrderDao mockOrderDao() {
        return OrderDao.builder()
                .fanEmail("test@gmail.com")
                .numberOfTickets(10)
                .gameName("Test game")
                .stadiumName("Test stadium")
                .gameDate(LocalDate.of(2023, 6, 8))
                .gameHour(LocalTime.of(18, 0, 0))
                .build();
    }

    public static OrderDetailsDao mockOrderDetailsDao() {
        return OrderDetailsDao.builder()
                .gameName("Test game")
                .stadiumName("Test stadium")
                .gameDate(LocalDate.of(2023, 6, 8))
                .gameHour(LocalTime.of(18, 0, 0))
                .purchaseDate(LocalDate.now())
                .purchaseTime(LocalTime.now())
                .totalPrice(100)
                .tickets(List.of(TicketMocks.mockTicketDao()))
                .build();
    }

    public static Order mockOrder(Integer totalPrice, List<Ticket> tickets) {
        return Order.builder()
                .date(LocalDate.now())
                .hour(LocalTime.now())
                .totalPrice(totalPrice)
                .tickets(tickets)
                .build();
    }
}
