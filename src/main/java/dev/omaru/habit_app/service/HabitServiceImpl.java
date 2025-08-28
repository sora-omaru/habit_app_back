package dev.omaru.habit_app.service;
import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.repository.HabitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class HabitServiceImpl implements HabitService {
    //ここでRepository層をよんでいる。
    private final HabitRepository repo;

    public HabitServiceImpl(HabitRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<Habit> list(UUID userId, Pageable pageable) {
        PageRequest sorted = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return repo.findByUserId(userId, sorted);
    }

    @Override
    public Habit create(UUID userId, CreateHabitRequest req) {
        Habit h = new Habit();
        h.setUserId(userId);
        h.setTitle(req.getTitle());
        h.setCreatedAt(Instant.now());
        return repo.save(h);
    }

    @Override
    public Habit update(UUID userId, UUID habitId, CreateHabitRequest req) {
        Habit h = repo.findByIdAndUserId(habitId, userId).orElseThrow();
        h.setTitle(req.getTitle());
        return repo.save(h);
    }

    @Override
    public void delete(UUID userId, UUID habitId) {
        Habit h = repo.findByIdAndUserId(habitId, userId).orElseThrow();
        repo.delete(h);
    }
}



