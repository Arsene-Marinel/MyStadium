package com.stadium.mystadium.service;

import com.stadium.mystadium.dao.FanDao;
import com.stadium.mystadium.entity.Fan;
import com.stadium.mystadium.exception.UniqueConstraintException;
import com.stadium.mystadium.mapper.FanMapper;
import com.stadium.mystadium.repository.FanRepository;
import com.stadium.mystadium.utils.FanMocks;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.stadium.mystadium.utils.Constants.FANS_NOT_FOUND;
import static com.stadium.mystadium.utils.Constants.UNIQUE_CONSTRAINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FanServiceImplTest {

    @InjectMocks
    private FanServiceImpl fanService;

    @Mock
    private FanRepository fanRepository;

    @Mock
    private FanMapper fanMapper;

    @Test
    public void addFanTest() {
        FanDao fanDao = FanMocks.mockFanDao();
        Fan fan = FanMocks.mockFan();

        when(fanRepository.save(fan)).thenReturn(fan);
        when(fanMapper.mapToFan(fanDao)).thenReturn(fan);
        when(fanMapper.mapToFanDao(fan)).thenReturn(fanDao);
        FanDao result = fanService.add(fanDao);

        assertEquals(result.getName(), fanDao.getName());
        assertEquals(result.getEmail(), fanDao.getEmail());
    }

    @Test
    public void addFanThrowsUniqueConstraintExceptionTest() {
        FanDao fanDao = FanMocks.mockFanDao();
        Fan fan = FanMocks.mockFan();

        when(fanRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(fan));
        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> fanService.add(fanDao));
        assertEquals(String.format(UNIQUE_CONSTRAINT, "fan", "email"), uniqueConstraintException.getMessage());
    }

    @Test
    public void searchFansWithNullParamTest() {
        FanDao fanDao = FanMocks.mockFanDao();
        List<FanDao> fanDaoList = List.of(fanDao);
        Fan fan = FanMocks.mockFan();
        List<Fan> fans = List.of(fan);

        when(fanRepository.findAll()).thenReturn(fans);
        when(fanMapper.mapToFanDao(fan)).thenReturn(fanDao);
        List<FanDao> result = fanService.searchFans(null);

        assertEquals(result, fanDaoList);
    }

    @Test
    public void searchFansWithParamTest() {
        FanDao fanDao = FanMocks.mockFanDao();
        List<FanDao> fanDaoList = List.of(fanDao);
        Fan fan = FanMocks.mockFan();
        List<Fan> fans = List.of(fan);

        when(fanMapper.mapToFanDao(fan)).thenReturn(fanDao);
        when(fanRepository.searchByNameOrEmail("test")).thenReturn(fans);
        List<FanDao> result = fanService.searchFans("test");

        assertEquals(result, fanDaoList);
    }

    @Test
    public void searchFansThrowsEntityNotFoundExceptionTest() {
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> fanService.searchFans("test"));
        assertEquals(String.format(FANS_NOT_FOUND, "test"), entityNotFoundException.getMessage());
    }
}
