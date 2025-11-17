package com.dpatrones.proyecto.domain.model.post;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

import com.dpatrones.proyecto.domain.model.User;

/**
 * Interaction - Like, Follow (Patrón Observer: trigger de notificaciones)
 * Patrón GRASP: Bajo acoplamiento
 */
@Entity
@Table(name = "interactions")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private InteractionType type; // LIKE, FOLLOW, SHARE

    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public enum InteractionType {
        LIKE, FOLLOW, SHARE
    }
}
