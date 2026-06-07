package com.unir.socialhabits.repositories;

import com.unir.socialhabits.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HabitRepository extends JpaRepository<Habit, UUID> {

    List<Habit> findByUserId(UUID userId);

    List<Habit> findByUserIdAndDate(UUID userId, LocalDate date);
}