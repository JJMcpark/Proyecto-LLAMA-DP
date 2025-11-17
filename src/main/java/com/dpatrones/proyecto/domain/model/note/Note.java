package com.dpatrones.proyecto.domain.model.note;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.dpatrones.proyecto.domain.model.User;

/**
 * Entidad Note - Para apuntes personales con versiones
 * Patrón GRASP: Alta cohesión (responsabilidad clara)
 */
@Entity
@Table(name = "notes")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String subject; // Matemáticas, Programación, etc.

    @Builder.Default
    private Integer versionNumber = 1;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<NoteLink> links = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        this.versionNumber += 1;
    }
}
