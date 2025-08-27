// src/test/java/dev/omaru/habit_app/controller/HabitControllerTest.java
package dev.omaru.habit_app.controller;

import dev.omaru.habit_app.dto.CreateHabitRequest;
import dev.omaru.habit_app.domain.Habit;
import dev.omaru.habit_app.service.HabitService;
import dev.omaru.habit_app.support.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = HabitController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)     // セキュリティ系フィルタ無効
@Import(GlobalExceptionHandler.class)         // 例外ハンドラ読み込み
class HabitControllerTest {

    @Autowired MockMvc mvc;
    @MockBean HabitService service;           // Controller依存はモック

    static final String USER = "00000000-0000-0000-0000-000000000001";

    @Test
    void list_ok() throws Exception {
        Habit h = new Habit();
        h.setId(UUID.randomUUID());
        h.setUserId(UUID.fromString(USER));
        h.setTitle("朝ラン");
        h.setCreatedAt(Instant.now());

        Mockito.when(service.list(UUID.fromString(USER))).thenReturn(List.of(h));

        mvc.perform(get("/api/habits").header("X-UserId", USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void create_ok() throws Exception {
        Habit h = new Habit();
        h.setId(UUID.randomUUID());
        h.setUserId(UUID.fromString(USER));
        h.setTitle("朝ラン");
        h.setCreatedAt(Instant.now());

        Mockito.when(service.create(Mockito.eq(UUID.fromString(USER)),
                        Mockito.any(CreateHabitRequest.class)))
                .thenReturn(h);

        mvc.perform(post("/api/habits")
                        .header("X-UserId", USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"朝ラン\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void create_validation_error_blank_title() throws Exception {
        mvc.perform(post("/api/habits")
                        .header("X-UserId", USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }
}
