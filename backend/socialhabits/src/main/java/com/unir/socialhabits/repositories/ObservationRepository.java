package com.unir.socialhabits.repositories;

import com.unir.socialhabits.entities.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ObservationRepository extends JpaRepository<Observation, UUID> {

    List<Observation> findByUserId(UUID userId);
}