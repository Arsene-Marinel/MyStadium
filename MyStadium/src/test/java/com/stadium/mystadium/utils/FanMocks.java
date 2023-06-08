package com.stadium.mystadium.utils;

import com.stadium.mystadium.dao.FanDao;
import com.stadium.mystadium.entity.Fan;

public class FanMocks {

    public static FanDao mockFanDao() {
        return FanDao.builder()
                .name("Test fan")
                .email("test@gmail.com")
                .build();
    }

    public static Fan mockFan() {
        return Fan.builder()
                .id(1L)
                .name("Test fan")
                .email("test@gmail.com")
                .build();
    }
}
