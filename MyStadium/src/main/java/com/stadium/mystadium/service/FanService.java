package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.FanDao;

import java.util.List;

public interface FanService {

    List<FanDao> searchFans(String searchParam);
}
