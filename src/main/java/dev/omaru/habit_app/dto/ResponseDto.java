package dev.omaru.habit_app.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
public class HabitResponseDto {
    UUID id;
    String title;
    Instant createdAt;


    @Builder
    public HabitResponseDto(UUID id, String title, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}