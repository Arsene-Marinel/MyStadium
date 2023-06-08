package com.stadium.mystadium.entity;

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
@Table(name = "stadiums")
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer numberOfSections;

    private Integer rowsOfSection;

    private Integer seatsOfRow;

    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GameSchedule> schedules = new ArrayList<>();
}
