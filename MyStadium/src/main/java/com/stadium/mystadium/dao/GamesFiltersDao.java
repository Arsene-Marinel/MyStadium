package com.stadium.mystadium.dao;

import com.stadium.mystadium.enums.CompetitionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GamesFiltersDao {

    private String name;

    private LocalDate date;

    private CompetitionEnum competition;
}
