package com.dpatrones.proyecto.domain.model.post;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

import com.dpatrones.proyecto.domain.model.User;

/**
 * Entidad Post - Patrón State (Estados: DRAFT, PUBLISHED, ARCHIVED)
 * Patrón GRASP: Experto en información sobre publicaciones
 */
@Entity
@Table(name = "posts")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @Column(name = "type") // BLOG, QUESTION, RESOURCE
    private String postType;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Integer likes = 0;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
        this.status = PostStatus.DRAFT;
        this.postType = "BLOG";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public enum PostStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }
}
