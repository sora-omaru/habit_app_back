package dev.omaru.habit_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//アプリケーションを立ち上げるときに最初に呼ばれるアプリのエントリポイント
@SpringBootApplication
public class HabitAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitAppApplication.class, args);
	}

}
