package dev.omaru.habit_app.view;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.HabitResponseDto;
import dev.omaru.habit_app.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitView {

    public HabitResponseDto toDto(Habit h) {
        return new HabitResponseDto(h.getId(), h.getTitle(), h.getCreatedAt());
    }

    public List<HabitResponseDto> toDtoList(List<Habit> habits) {
        return habits.stream().map(this::toDto).toList();
    }

    public PageResponse<HabitResponseDto> toDtoPage(Page<Habit> page) {
        return PageResponse.<HabitResponseDto>builder()
                .items(page.getContent().stream().map(this::toDto).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .build();
    }
}
