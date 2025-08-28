package dev.omaru.habit_app.view;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.HabitResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitView {

    public HabitResponseDto toDto(Habit h) {
        return new HabitResponseDto(h.getId(),h.getTitle(),h.getCreatedAt());
    }

    public List<HabitResponseDto> toDtoList(List<Habit> habits) {
        return  habits.stream().map(this::toDto).toList();
    }
}
