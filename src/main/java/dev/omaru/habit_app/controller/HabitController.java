package dev.omaru.habit_app.controller;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.dto.HabitResponseDto;
import dev.omaru.habit_app.dto.PageResponse;
import dev.omaru.habit_app.service.HabitService;
import dev.omaru.habit_app.view.HabitView;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController//これによって戻り値がそのままJSONになる
@RequestMapping("/api/habits")

public class HabitController {
    //フィールド作成
    private final HabitService service;

    //コンストラクタ作成␊
    public HabitController(HabitService service) {
        this.service = service;
    }

    //仮の認証：ユーザーIDは一時的にヘッダから受け取る
    @GetMapping
    public PageResponse<HabitResponseDto> list(
            @RequestHeader("X-UserId") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Habit> habits = service.list(userId, page, size);
        return view.toDtoPage(habits);//ここでJsonとして情報が返ってくる
    }

    @PostMapping
    public ResponseEntity<Habit> create(@RequestHeader("X-UserId") UUID userId,
                                        @Valid @RequestBody CreateHabitRequest req) {
        Habit saved = service.create(userId, req);
        URI location = URI.create("/api/habits/" + saved.getId());
        return ResponseEntity.created(location).body(view.toDto(saved));
    }

    @PutMapping("/{id}")
    public HabitResponseDto update(
            @RequestHeader("X-UserId") UUID userId,
            @PathVariable UUID id,
            @Valid @RequestBody CreateHabitRequest req) {
        Habit updated = service.update(userId, id, req);
        return view.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-UserId") UUID userId,
            @PathVariable UUID id) {
        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}


