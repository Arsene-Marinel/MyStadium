package com.stadium.mystadium.mapper;

import com.stadium.mystadium.dao.OrderDetailsDao;
import com.stadium.mystadium.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final TicketMapper ticketMapper;

    @Autowired
    public OrderMapper(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    public OrderDetailsDao mapToOrderDetailsDao(Order order) {
        return OrderDetailsDao.builder()
                .gameName(order.getTickets().get(0).getGameSchedule().getGame().getName())
                .stadiumName(order.getTickets().get(0).getGameSchedule().getStadium().getName())
                .gameDate(order.getTickets().get(0).getGameSchedule().getId().getDate())
                .gameHour(order.getTickets().get(0).getGameSchedule().getId().getHour())
                .purchaseDate(order.getDate())
                .purchaseTime(order.getHour())
                .totalPrice(order.getTotalPrice())
                .tickets(order.getTickets().stream().map(ticketMapper::mapToTicketDao).toList())
                .build();
    }
}
