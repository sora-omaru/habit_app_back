package dev.omaru.habit_app.service;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

//Controllerを薄くたもとつためのservice層(interface)
public interface HabitService {
    Page<Habit> list(UUID userId, int page, int size);
    Habit create(UUID userId, CreateHabitRequest req);
    Habit update(UUID userId, UUID habitId, CreateHabitRequest req);
    void delete(UUID userId, UUID habitId);
}



