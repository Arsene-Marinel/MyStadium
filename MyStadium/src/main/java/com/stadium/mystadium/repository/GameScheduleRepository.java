package com.stadium.mystadium.repository;

import com.stadium.mystadium.composed_id.GameScheduleId;
import com.stadium.mystadium.entity.GameSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameScheduleRepository extends JpaRepository<GameSchedule, GameScheduleId> {
}
