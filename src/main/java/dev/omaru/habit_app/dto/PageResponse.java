package dev.omaru.habit_app.dto;
import lombok.Value;
import lombok.Builder;

import java.util.List;

public class PageResponse<T>{
    List<T> items;
    int page;
    int size;
    long totalElements;
    int totalPages;
    boolean hasNext;

}
