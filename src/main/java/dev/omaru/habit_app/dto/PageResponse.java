package dev.omaru.habit_app.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PageResponse<T> {
    List<T> items;
    int page;
    int size;
    long totalElements;
    int totalPages;
    boolean hasNext;
}