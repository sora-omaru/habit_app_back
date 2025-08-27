package dev.omaru.habit_app.service;

import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.domain.Habit;

import java.util.List;
import java.util.UUID;

//Controllerを薄くたもとつためのservice層
public interface HabitService {
    List<Habit> list(UUID userId);
    Habit create(UUID userId, CreateHabitRequest req);
}



