package com.stadium.mystadium.utils;

import com.stadium.mystadium.dao.TicketDao;
import com.stadium.mystadium.entity.GameSchedule;
import com.stadium.mystadium.entity.Ticket;

public class TicketMocks {

    public static TicketDao mockTicketDao() {
        return TicketDao.builder()
                .section(7)
                .row(2)
                .seat(1)
                .build();
    }

    public static Ticket mockTicket(GameSchedule gameSchedule) {
        return Ticket.builder()
                .section(7)
                .row(1)
                .seat(1)
                .gameSchedule(gameSchedule)
                .build();
    }
}
