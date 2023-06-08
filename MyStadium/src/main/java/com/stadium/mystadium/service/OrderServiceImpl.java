package com.stadium.mystadium.service;

import com.stadium.mystadium.composed_id.GameScheduleId;
import com.stadium.mystadium.dao.OrderDao;
import com.stadium.mystadium.dao.OrderDetailsDao;
import com.stadium.mystadium.entity.*;
import com.stadium.mystadium.exception.BadRequestException;
import com.stadium.mystadium.mapper.OrderMapper;
import com.stadium.mystadium.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stadium.mystadium.utils.Constants.*;

@Service
public class OrderServiceImpl implements CommonService<OrderDetailsDao, OrderDao> {

    private final OrderRepository orderRepository;

    private final FanRepository fanRepository;

    private final GameRepository gameRepository;

    private final StadiumRepository stadiumRepository;

    private final GameScheduleRepository gameScheduleRepository;

    private final TicketRepository ticketRepository;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, FanRepository fanRepository, GameRepository gameRepository, StadiumRepository stadiumRepository, GameScheduleRepository gameScheduleRepository, TicketRepository ticketRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.fanRepository = fanRepository;
        this.gameRepository = gameRepository;
        this.stadiumRepository = stadiumRepository;
        this.gameScheduleRepository = gameScheduleRepository;
        this.ticketRepository = ticketRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderDetailsDao add(OrderDao orderDao) {
        Optional<Fan> optionalFan = fanRepository.findByEmail(orderDao.getFanEmail());
        if(optionalFan.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "fan", "email", orderDao.getFanEmail()));
        }

        Optional<Game> optionalGame = gameRepository.findByName(orderDao.getGameName());
        if(optionalGame.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "game", "name", orderDao.getGameName()));
        }

        Optional<Stadium> optionalStadium = stadiumRepository.findByName(orderDao.getStadiumName());
        if(optionalStadium.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "stadium", "name", orderDao.getStadiumName()));
        }

        Game game = optionalGame.get();
        Stadium stadium = optionalStadium.get();
        GameScheduleId gameScheduleId = GameScheduleId.builder()
                .gameId(game.getId())
                .stadiumId(stadium.getId())
                .date(orderDao.getGameDate())
                .hour(orderDao.getGameHour())
                .build();
        Optional<GameSchedule> optionalGameSchedule = gameScheduleRepository.findById(gameScheduleId);
        if(optionalGameSchedule.isEmpty()) {
            throw new BadRequestException(GAME_NOT_SCHEDULED);
        }

        if(LocalDateTime.of(orderDao.getGameDate(), orderDao.getGameHour()).isBefore(LocalDateTime.now())) {
            throw new BadRequestException(PAST_RESERVATION);
        }

        GameSchedule gameSchedule = optionalGameSchedule.get();
        int stadiumCapacity = stadium.getNumberOfSections() * stadium.getRowsOfSection() * stadium.getSeatsOfRow();
        int soldTickets = gameSchedule.getTickets().size();
        if(stadiumCapacity - soldTickets < orderDao.getNumberOfTickets()) {
            throw new BadRequestException(NOT_ENOUGH_TICKETS);
        }

        Optional<Ticket> optionalLastTicket = ticketRepository.findLastTicketForGame(gameSchedule);
        int row = 1;
        int section = 1;
        int seat = 0;
        if(optionalLastTicket.isPresent()) {
            section = optionalLastTicket.get().getSection();
            row = optionalLastTicket.get().getRow();
            seat = optionalLastTicket.get().getSeat();
        }

        List<Ticket> tickets = new ArrayList<>();
        for(int i = 0; i < orderDao.getNumberOfTickets(); i++) {
            seat++;
            if(seat > stadium.getSeatsOfRow()) {
                row++;
                seat = 1;
            }
            if(row > stadium.getRowsOfSection()) {
                section++;
                row = 1;
            }
            Ticket ticket = Ticket.builder()
                    .section(section)
                    .row(row)
                    .seat(seat)
                    .gameSchedule(gameSchedule)
                    .build();
            tickets.add(ticket);
        }

        Order order = Order.builder()
                .totalPrice(gameSchedule.getPrice() * tickets.size())
                .date(LocalDate.now())
                .hour(LocalTime.now())
                .tickets(tickets)
                .build();
        return orderMapper.mapToOrderDetailsDao(orderRepository.save(order));
    }
}
