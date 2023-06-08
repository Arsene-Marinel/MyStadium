package com.stadium.mystadium.service;


import com.stadium.mystadium.dao.StadiumDao;
import com.stadium.mystadium.dao.StadiumDetailsDao;
import com.stadium.mystadium.entity.Stadium;
import com.stadium.mystadium.exception.UniqueConstraintException;
import com.stadium.mystadium.mapper.StadiumMapper;
import com.stadium.mystadium.repository.StadiumRepository;
import com.stadium.mystadium.utils.StadiumMocks;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.stadium.mystadium.utils.Constants.ENTITY_NOT_FOUND;
import static com.stadium.mystadium.utils.Constants.UNIQUE_CONSTRAINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StadiumServiceImplTest {

    @InjectMocks
    private StadiumServiceImpl stadiumService;

    @Mock
    private StadiumRepository stadiumRepository;

    @Mock
    private StadiumMapper stadiumMapper;

    @Test
    public void getRoomsTest() {
        StadiumDetailsDao stadiumDetailsDao = StadiumMocks.mockStadiumDetailsDao();
        List<StadiumDetailsDao> stadiumDetailsDaoList = List.of(stadiumDetailsDao);
        Stadium stadium = StadiumMocks.mockStadium();
        List<Stadium> stadiums = List.of(stadium);

        when(stadiumRepository.findAll()).thenReturn(stadiums);
        when(stadiumMapper.mapToStadiumDetailsDao(stadium)).thenReturn(stadiumDetailsDao);

        List<StadiumDetailsDao> result = stadiumService.getStadiums();
        assertEquals(result, stadiumDetailsDaoList);
    }

    @Test
    public void addStadiumTest() {
        Stadium stadium = StadiumMocks.mockStadium();
        StadiumDao stadiumDao = StadiumMocks.mockStadiumDao();

        when(stadiumRepository.save(stadium)).thenReturn(stadium);
        when(stadiumMapper.mapToStadium(stadiumDao)).thenReturn(stadium);
        when(stadiumMapper.mapToStadiumDao(stadium)).thenReturn(stadiumDao);
        StadiumDao result = stadiumService.add(stadiumDao);

        assertEquals(result.getName(), stadiumDao.getName());
        assertEquals(result.getNumberOfSections(), stadiumDao.getNumberOfSections());
        assertEquals(result.getRowsOfSection(), stadiumDao.getRowsOfSection());
        assertEquals(result.getSeatsOfRow(), stadiumDao.getSeatsOfRow());
    }

    @Test
    public void addStadiumThrowsUniqueConstraintExceptionTest() {
        StadiumDao stadiumDao = StadiumMocks.mockStadiumDao();
        Stadium stadium = StadiumMocks.mockStadium();
        when(stadiumRepository.findByName("Test stadium")).thenReturn(Optional.of(stadium));

        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> stadiumService.add(stadiumDao));
        assertEquals(String.format(UNIQUE_CONSTRAINT, "stadium", "name"), uniqueConstraintException.getMessage());
    }

    @Test
    public void editStadiumTest() {
        Stadium stadium = StadiumMocks.mockStadium();
        StadiumDao stadiumDao = StadiumMocks.mockStadiumDao();
        stadium.setName("Test stadium edit");

        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(stadium));
        stadiumDao.setName("Test stadium edit");
        when(stadiumMapper.mapToStadiumDao(stadium)).thenReturn(stadiumDao);
        when(stadiumRepository.save(stadium)).thenReturn(stadium);
        StadiumDao result = stadiumService.editStadium(1L, stadiumDao);

        assertEquals(result.getName(), stadiumDao.getName());
        assertEquals(result.getName(), "Test stadium edit");
    }

    @Test
    public void editStadiumThrowsEntityNotFoundExceptionTest() {
        Stadium stadium = StadiumMocks.mockStadium();
        StadiumDao stadiumDao = StadiumMocks.mockStadiumDao();
        stadium.setName("Test stadium edit");

        when(stadiumRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> stadiumService.editStadium(1L, stadiumDao));
        assertEquals(String.format(ENTITY_NOT_FOUND, "stadium", "id", 1), entityNotFoundException.getMessage());
    }

    @Test
    public void editStadiumThrowsUniqueConstraintExceptionTest() {
        Stadium stadium = StadiumMocks.mockStadium();
        StadiumDao stadiumDao = StadiumMocks.mockStadiumDao();
        stadiumDao.setName("Test stadium edit");
        stadium.setName("Test stadium edit");
        Stadium testStadium = StadiumMocks.mockStadium();
        testStadium.setId(2L);
        testStadium.setName("Test stadium edit");

        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(stadium));
        when(stadiumRepository.findByName("Test stadium edit")).thenReturn(Optional.of(testStadium));
        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> stadiumService.editStadium(1L, stadiumDao));
        assertEquals(String.format(UNIQUE_CONSTRAINT, "stadium", "name"), uniqueConstraintException.getMessage());
    }
}
