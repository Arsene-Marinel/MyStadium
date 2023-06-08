package com.stadium.mystadium.entity;

import com.stadium.mystadium.composed_id.GameScheduleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games_schedule")
public class GameSchedule {

    @EmbeddedId
    private GameScheduleId id;

    private Integer price;

    @MapsId("stadiumId")
    @ManyToOne
    private Stadium stadium;

    @MapsId("gameId")
    @OneToOne
    private Game game;

    @OneToMany(mappedBy = "gameSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();
}
