package dev.omaru.habit_app.controller;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.service.HabitService;
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

    //コンストラクタ作成␊
    public HabitController(HabitService service) {
        this.service = service;
    }

    //仮の認証：ユーザーIDは一時的にヘッダから受け取る
    @GetMapping
    public List<Habit> list(@RequestHeader("X-UserId") UUID userId) {
        return service.list(userId);
    }

    @PostMapping
    public ResponseEntity<Habit> create(@RequestHeader("X-UserId") UUID userId,
                                        @Valid @RequestBody CreateHabitRequest req) {
        Habit saved = service.create(userId, req);
        URI location = URI.create("/api/habits/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }
}


