package dev.omaru.habit_app.service;

import dev.omaru.habit_app.domain.Habit;
import  dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.time.Instant;
@Service
public class HabitServiceImpl implements HabitService {
    //ここでRepository層をよんでいる。
    private final HabitRepository repo;
    public HabitServiceImpl(HabitRepository repo) {this.repo = repo;}

    @Override
    public List<Habit>list(UUID userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Habit create(UUID userId, CreateHabitRequest req) {
        Habit h = new Habit();
        h.setUserId(userId);
        h.setTitle(req.getTitle());
        h.setCreatedAt(Instant.now());
        return repo.save(h);
    }
}




