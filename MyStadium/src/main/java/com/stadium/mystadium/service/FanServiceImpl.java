package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.FanDao;
import com.stadium.mystadium.entity.Fan;
import com.stadium.mystadium.exception.UniqueConstraintException;
import com.stadium.mystadium.mapper.FanMapper;
import com.stadium.mystadium.repository.FanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.stadium.mystadium.utils.Constants.FANS_NOT_FOUND;
import static com.stadium.mystadium.utils.Constants.UNIQUE_CONSTRAINT;

@Service
public class FanServiceImpl implements FanService, CommonService<FanDao, FanDao> {

    private final FanRepository fanRepository;

    private final FanMapper fanMapper;

    @Autowired
    public FanServiceImpl(FanRepository fanRepository, FanMapper fanMapper) {
        this.fanRepository = fanRepository;
        this.fanMapper = fanMapper;
    }

    @Override
    public FanDao add(FanDao fanDao) {
        Optional<Fan> optionalFan = fanRepository.findByEmail(fanDao.getEmail());
        if(optionalFan.isPresent()) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, "fan", "email"));
        }
        return fanMapper.mapToFanDao(fanRepository.save(fanMapper.mapToFan(fanDao)));
    }

    @Override
    public List<FanDao> searchFans(String searchParam) {
        if(searchParam == null) {
            return fanRepository.findAll().stream().map(fanMapper::mapToFanDao).toList();
        }

        searchParam = searchParam.toLowerCase();
        List<Fan> fans = fanRepository.searchByNameOrEmail(searchParam);
        if(fans.isEmpty()) {
            throw new EntityNotFoundException(String.format(FANS_NOT_FOUND, searchParam));
        }
        return fans.stream().map(fanMapper::mapToFanDao).toList();
    }
}
