package com.stadium.mystadium.mapper;

import com.stadium.mystadium.dao.GameScheduleDao;
import com.stadium.mystadium.entity.GameSchedule;
import org.springframework.stereotype.Component;

@Component
public class GameScheduleMapper {
    public GameScheduleDao mapToGameScheduleDao(GameSchedule gameSchedule) {
        return GameScheduleDao.builder()
                .gameName(gameSchedule.getGame().getName())
                .stadiumName(gameSchedule.getStadium().getName())
                .date(gameSchedule.getId().getDate())
                .hour(gameSchedule.getId().getHour())
                .price(gameSchedule.getPrice())
                .build();
    }
}
