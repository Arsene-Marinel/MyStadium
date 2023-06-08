package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.OrderDao;
import com.stadium.mystadium.dao.OrderDetailsDao;
import com.stadium.mystadium.entity.*;
import com.stadium.mystadium.mapper.OrderMapper;
import com.stadium.mystadium.repository.*;
import com.stadium.mystadium.utils.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private FanRepository fanRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private StadiumRepository stadiumRepository;

    @Mock
    private GameScheduleRepository gameScheduleRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private OrderMapper orderMapper;

    @Test
    public void addOrderTest() {
        Fan fan = FanMocks.mockFan();
        Game game = GameMocks.mockGame();
        Stadium stadium = StadiumMocks.mockStadium();
        GameSchedule gameSchedule = GameScheduleMocks.mockGameSchedule(game, stadium);
        Ticket ticket = TicketMocks.mockTicket(gameSchedule);
        Order order = OrderMocks.mockOrder(gameSchedule.getPrice(), List.of(ticket));
        OrderDetailsDao orderDetailsDao = OrderMocks.mockOrderDetailsDao();
        OrderDao orderDao = OrderMocks.mockOrderDao();
        Ticket lastTicket = TicketMocks.mockTicket(gameSchedule);
        lastTicket.setSeat(stadium.getSeatsOfRow());

        when(fanRepository.findByEmail(fan.getEmail())).thenReturn(Optional.of(fan));
        when(gameRepository.findByName(game.getName())).thenReturn(Optional.of(game));
        when(stadiumRepository.findByName(stadium.getName())).thenReturn(Optional.of(stadium));
        when(gameScheduleRepository.findById(gameSchedule.getId())).thenReturn(Optional.of(gameSchedule));
        when(ticketRepository.findLastTicketForGame(gameSchedule)).thenReturn(Optional.of(lastTicket));
        when(orderRepository.save(any())).thenReturn(order);    // any() because using order would cause an error due to exact time
        when(orderMapper.mapToOrderDetailsDao(order)).thenReturn(orderDetailsDao);
        OrderDetailsDao result = orderService.add(orderDao);

        assertEquals(result, orderDetailsDao);
    }
}
