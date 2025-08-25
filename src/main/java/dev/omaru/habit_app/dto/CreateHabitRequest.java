package dev.omaru.habit_app.dto;

//recotd版
//import jakarta.validation.constraints.NotNull;
//
//public record CreateHabitRequest(@NotNull String title) {
//
//}

//通常版
//
//import jakarta.validation.constraints.NotNull;
//// もし空文字も弾きたいなら↓を使う
//// import jakarta.validation.constraints.NotBlank;
//
//public class CreateHabitRequest {
//    @NotNull
//    private String title;
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//}

//Lombokを使った版(1番シンプル)
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class CreateHabitRequest {
    @NotBlank
    private String title;
}

