package com.stadium.mystadium.mapper;

import com.stadium.mystadium.dao.StadiumDao;
import com.stadium.mystadium.dao.StadiumDetailsDao;
import com.stadium.mystadium.entity.Stadium;
import org.springframework.stereotype.Component;

@Component
public class StadiumMapper {

    public StadiumDao mapToStadiumDao(Stadium stadium) {
        return StadiumDao.builder()
                .name(stadium.getName())
                .numberOfSections(stadium.getNumberOfSections())
                .rowsOfSection(stadium.getRowsOfSection())
                .seatsOfRow(stadium.getSeatsOfRow())
                .build();
    }

    public Stadium mapToStadium(StadiumDao stadiumDao) {
        return Stadium.builder()
                .name(stadiumDao.getName())
                .numberOfSections(stadiumDao.getNumberOfSections())
                .rowsOfSection(stadiumDao.getRowsOfSection())
                .seatsOfRow(stadiumDao.getSeatsOfRow())
                .build();
    }

    public StadiumDetailsDao mapToStadiumDetailsDao(Stadium stadium) {
        return StadiumDetailsDao.builder()
                .id(stadium.getId())
                .name(stadium.getName())
                .numberOfSections(stadium.getNumberOfSections())
                .rowsOfSection(stadium.getRowsOfSection())
                .seatsOfRow(stadium.getSeatsOfRow())
                .build();
    }
}
