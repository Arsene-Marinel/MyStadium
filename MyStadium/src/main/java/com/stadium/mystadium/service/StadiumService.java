package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.StadiumDao;
import com.stadium.mystadium.dao.StadiumDetailsDao;

import java.util.List;

public interface StadiumService {

    List<StadiumDetailsDao> getStadiums();

    StadiumDao editStadium(Long id, StadiumDao stadiumDao);
}
