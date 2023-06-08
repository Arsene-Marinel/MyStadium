package com.stadium.mystadium.mapper;

import com.stadium.mystadium.dao.TicketDao;
import com.stadium.mystadium.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketDao mapToTicketDao(Ticket ticket) {
        return TicketDao.builder()
                .section(ticket.getSection())
                .row(ticket.getRow())
                .seat(ticket.getSeat())
                .build();
    }
}
