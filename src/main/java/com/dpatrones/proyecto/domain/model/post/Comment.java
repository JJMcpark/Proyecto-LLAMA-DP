package com.dpatrones.proyecto.domain.model.post;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

import com.dpatrones.proyecto.domain.model.User;

/**
 * Entidad Comment - Comentarios en Posts
 * Patrón GRASP: Bajo acoplamiento (solo relaciona User y Post)
 */
@Entity
@Table(name = "comments")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
