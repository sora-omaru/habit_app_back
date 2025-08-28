package dev.omaru.habit_app.controller;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.service.HabitService;
import dev.omaru.habit_app.view.HabitView;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HabitController.class)
@Import({HabitView.class}) // View層は実物を使う
class HabitControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    HabitService service;

    @Test
    void list_returnsDtoArray() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Instant now = Instant.parse("2025-08-22T00:00:00Z");

        Habit h = new Habit();
        h.setId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        h.setUserId(userId);
        h.setTitle("Run 5 min");
        h.setCreatedAt(now);

        given(service.list(eq(userId))).willReturn(List.of(h));

        mvc.perform(get("/api/habits")
                        .header("X-UserId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .andExpect(jsonPath("$[0].title").value("Run 5 min"))
                .andExpect(jsonPath("$[0].createdAt").value("2025-08-22T00:00:00Z"));
    }

    @Test
    void create_returns201_withLocation_andDto() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID newId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        Instant now = Instant.parse("2025-08-23T00:00:00Z");

        Habit saved = new Habit();
        saved.setId(newId);
        saved.setUserId(userId);
        saved.setTitle("Read book");
        saved.setCreatedAt(now);

        given(service.create(eq(userId), any(CreateHabitRequest.class))).willReturn(saved);

        mvc.perform(post("/api/habits")
                        .header("X-UserId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                         {"title":"Read book"}
                         """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/habits/" + newId))
                .andExpect(jsonPath("$.id").value(newId.toString()))
                .andExpect(jsonPath("$.title").value("Read book"))
                .andExpect(jsonPath("$.createdAt").value("2025-08-23T00:00:00Z"));
    }

    @Test
    void create_validationError_returns400_problemDetail() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        mvc.perform(post("/api/habits")
                        .header("X-UserId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // title欠落
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/habits"));
    }
}
