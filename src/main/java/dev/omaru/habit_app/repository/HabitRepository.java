package dev.omaru.habit_app.repository;

import dev.omaru.habit_app.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import  org.springframework.data.domain.Page;
import  org.springframework.data.domain.Pageable;

public interface HabitRepository extends JpaRepository<Habit,UUID> {
    List<Habit> findByUserId(UUID userId);
    // ユーザーIDで絞り込み＋createdAtの降順ソート
    List<Habit> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Page<Habit> findByUserId(UUID userId, Pageable pageable);
    Optional<Habit> findByIdAndUserId(UUID id, UUID userId);
}

