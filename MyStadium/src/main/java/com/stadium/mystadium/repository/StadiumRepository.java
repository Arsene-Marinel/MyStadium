package com.stadium.mystadium.repository;

import com.stadium.mystadium.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {

    Optional<Stadium> findByName(String name);
}
