package dev.omaru.habit_app.controller;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.dto.HabitResponseDto;
import dev.omaru.habit_app.service.HabitService;
import dev.omaru.habit_app.view.HabitView;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController//これによって戻り値がそのままJSONになる
@RequestMapping("/api/habits")

public class HabitController {
    //フィールド作成
    private final HabitService service;
    private final HabitView view;

    //コンストラクタ作成␊
    public HabitController(HabitService service , HabitView view) {
        this.service = service;
        this.view = view;
    }

    //仮の認証：ユーザーIDは一時的にヘッダから受け取る
    @GetMapping
    public List<HabitResponseDto> list(@RequestHeader("X-UserId") UUID userId) {
        List<Habit> habits = service.list(userId);
        return view.toDtoList(habits);//ここでJsonとして情報が返ってくる
    }

    @PostMapping
    public ResponseEntity<HabitResponseDto> create(@RequestHeader("X-UserId") UUID userId,
                                        @Valid @RequestBody CreateHabitRequest req) {
        Habit saved = service.create(userId, req);
        URI location = URI.create("/api/habits/" + saved.getId());
        return ResponseEntity.created(location).body(view.toDto(saved));
    }
}


