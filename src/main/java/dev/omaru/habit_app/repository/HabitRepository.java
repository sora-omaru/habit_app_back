package dev.omaru.habit_app.repository;

import dev.omaru.habit_app.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface HabitRepository extends JpaRepository<Habit,UUID> {
List<Habit> findByUserId(UUID userId);
    // ユーザーIDで絞り込み＋createdAtの降順ソート
    List<Habit> findByUserIdOrderByCreatedAtDesc(UUID userId);
}

