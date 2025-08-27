package dev.omaru.habit_app.domain;

import jakarta.persistence.*;//JPAのアノテーション機能を利用するため(@Entity,@IDなど、エンティティとDBのマッピングをします)
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;//日時を表すもの
import java.util.UUID;//主キーをこれに設定するため

@Entity
@Table(name = "habits")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }

    //getter/setter
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Instant now) {

    }
}
