package dev.omaru.habit_app.dto;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PageResponse<T>{
    List<T> list;
    int page;
    int size;
    int totalElements;
    int totalPages;
    boolean hasNext;
}

