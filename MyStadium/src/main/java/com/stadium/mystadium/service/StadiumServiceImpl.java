package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.StadiumDao;
import com.stadium.mystadium.dao.StadiumDetailsDao;
import com.stadium.mystadium.entity.Stadium;
import com.stadium.mystadium.exception.UniqueConstraintException;
import com.stadium.mystadium.mapper.StadiumMapper;
import com.stadium.mystadium.repository.StadiumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stadium.mystadium.utils.Constants.ENTITY_NOT_FOUND;
import static com.stadium.mystadium.utils.Constants.UNIQUE_CONSTRAINT;

@Service
public class StadiumServiceImpl implements StadiumService, CommonService<StadiumDao, StadiumDao> {

    private final StadiumRepository stadiumRepository;

    private final StadiumMapper stadiumMapper;

    @Autowired
    public StadiumServiceImpl(StadiumRepository stadiumRepository, StadiumMapper stadiumMapper) {
        this.stadiumRepository = stadiumRepository;
        this.stadiumMapper = stadiumMapper;
    }

    @Override
    public List<StadiumDetailsDao> getStadiums() {
        return stadiumRepository.findAll().stream().map(stadiumMapper::mapToStadiumDetailsDao).collect(Collectors.toList());
    }

    @Override
    public StadiumDao add(StadiumDao stadiumDao) {
        Optional<Stadium> optionalStadium = stadiumRepository.findByName(stadiumDao.getName());
        if (optionalStadium.isPresent()) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, "stadium", "name"));
        }
        return stadiumMapper.mapToStadiumDao(stadiumRepository.save(stadiumMapper.mapToStadium(stadiumDao)));
    }

    @Override
    public StadiumDao editStadium(Long id, StadiumDao stadiumDao) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(id);
        if (optionalStadium.isEmpty()) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "stadium", "id", id));
        }

        Optional<Stadium> optionalStadiumByName = stadiumRepository.findByName(stadiumDao.getName());
        if (optionalStadiumByName.isPresent() && !Objects.equals(optionalStadiumByName.get().getId(), id)) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, "stadium", "name"));
        }

        Stadium stadium = optionalStadium.get();
        stadium.setName(stadiumDao.getName());
        stadium.setNumberOfSections(stadiumDao.getNumberOfSections());
        stadium.setRowsOfSection(stadiumDao.getRowsOfSection());
        stadium.setSeatsOfRow(stadiumDao.getSeatsOfRow());

        return stadiumMapper.mapToStadiumDao(stadiumRepository.save(stadium));
    }
}
