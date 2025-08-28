package dev.omaru.habit_app.controller;

import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.service.HabitService;
import dev.omaru.habit_app.view.HabitView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
    void list_returnsPageResponse() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Instant now = Instant.parse("2025-08-22T00:00:00Z");

        Habit h = new Habit();
        h.setId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        h.setUserId(userId);
        h.setTitle("Run 5 min");
        h.setCreatedAt(now);

        given(service.list(eq(userId), eq(0), eq(10)))
                .willReturn(new PageImpl<>(List.of(h), PageRequest.of(0, 10), 1));

        mvc.perform(get("/api/habits")
                        .header("X-UserId", userId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .andExpect(jsonPath("$.items[0].title").value("Run 5 min"))
                .andExpect(jsonPath("$.items[0].createdAt").value("2025-08-22T00:00:00Z"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));
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
    void update_returnsDto() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID id = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
        Instant now = Instant.parse("2025-08-24T00:00:00Z");

        Habit updated = new Habit();
        updated.setId(id);
        updated.setUserId(userId);
        updated.setTitle("New Title");
        updated.setCreatedAt(now);

        given(service.update(eq(userId), eq(id), any(CreateHabitRequest.class))).willReturn(updated);

        mvc.perform(put("/api/habits/" + id)
                        .header("X-UserId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                         {"title":"New Title"}
                         """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.createdAt").value("2025-08-24T00:00:00Z"));
    }

    @Test
    void delete_returns204() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID id = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

        willDoNothing().given(service).delete(userId, id);

        mvc.perform(delete("/api/habits/" + id)
                        .header("X-UserId", userId))
                .andExpect(status().isNoContent());
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
