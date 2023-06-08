package com.stadium.mystadium.entity;

import com.stadium.mystadium.enums.SportEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer numberOfTShirt;

    private SportEnum sportPlayer;

    @ManyToMany(mappedBy = "players")
    private List<Game> games;
}
