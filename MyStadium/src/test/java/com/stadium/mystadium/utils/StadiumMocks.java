package com.stadium.mystadium.utils;

import com.stadium.mystadium.dao.StadiumDao;
import com.stadium.mystadium.dao.StadiumDetailsDao;
import com.stadium.mystadium.entity.Stadium;

import java.util.ArrayList;

public class StadiumMocks {

    public static StadiumDao mockStadiumDao() {
        return StadiumDao.builder()
                .name("Test stadium")
                .numberOfSections(4)
                .rowsOfSection(12)
                .seatsOfRow(20)
                .build();
    }

    public static StadiumDetailsDao mockStadiumDetailsDao() {
        return StadiumDetailsDao.builder()
                .id(1L)
                .name("Test stadium")
                .numberOfSections(4)
                .rowsOfSection(12)
                .seatsOfRow(20)
                .build();
    }

    public static StadiumDetailsDao mockStadiumDetailsDao(Long id) {
        return StadiumDetailsDao.builder()
                .id(id)
                .name("Test stadium " + id)
                .numberOfSections(id.intValue() * 4)
                .rowsOfSection(id.intValue() * 12)
                .seatsOfRow(id.intValue() * 20)
                .build();
    }

    public static Stadium mockStadium() {
        return Stadium.builder()
                .id(1L)
                .name("Test stadium")
                .numberOfSections(4)
                .rowsOfSection(12)
                .seatsOfRow(10)
                .schedules(new ArrayList<>())
                .build();
    }
}

