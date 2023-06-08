package com.stadium.mystadium.repository;

import com.stadium.mystadium.entity.GameSchedule;
import com.stadium.mystadium.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.gameSchedule = ?1 ORDER BY t.section, t.row, t.seat DESC LIMIT 1")
    Optional<Ticket> findLastTicketForGame(GameSchedule gameSchedule);
}
