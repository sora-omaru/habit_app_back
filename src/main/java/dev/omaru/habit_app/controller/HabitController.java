package dev.omaru.habit_app.controller;
import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.repository.HabitRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController//これによって戻り値がそのままJSONになる
@RequestMapping("/api/habits")

public class HabitController {
    //フィールド作成
    private final HabitRepository repo;
    //コンストラクタ作成
    public HabitController(HabitRepository repo) {this.repo = repo;}

    //仮の認証：ユーザーIDは一時的にヘッダから受け取る
    @GetMapping
    public List<Habit> list(@RequestHeader("X-UserId") UUID userId) {
        return repo.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Habit create(@RequestHeader("X-UserId") UUID userId,@Valid @RequestBody CreateHabitRequest req) {
        Habit h = new Habit();
        h.setUserId(userId);
        h.setTitle(req.getTitle());
        return repo.save(h);
    }
}


