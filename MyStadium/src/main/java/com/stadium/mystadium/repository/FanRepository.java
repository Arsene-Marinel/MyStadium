package com.stadium.mystadium.repository;

import com.stadium.mystadium.entity.Fan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FanRepository extends JpaRepository<Fan, Long> {

    Optional<Fan> findByEmail(String email);

    @Query("SELECT c FROM Fan c WHERE LOWER(c.name) LIKE '%' || ?1 || '%' OR LOWER(c.email) LIKE '%' || ?1 || '%'")
    List<Fan> searchByNameOrEmail(String searchParam);
}
