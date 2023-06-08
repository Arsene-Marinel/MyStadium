package com.stadium.mystadium.mapper;

import com.stadium.mystadium.dao.FanDao;
import com.stadium.mystadium.entity.Fan;
import org.springframework.stereotype.Component;

@Component
public class FanMapper {

    public FanDao mapToFanDao(Fan fan) {
        return FanDao.builder()
                .name(fan.getName())
                .email(fan.getEmail())
                .build();
    }

    public Fan mapToFan(FanDao fanDao) {
        return Fan.builder()
                .name(fanDao.getName())
                .email(fanDao.getEmail())
                .build();
    }
}
