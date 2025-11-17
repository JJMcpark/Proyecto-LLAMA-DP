package com.dpatrones.proyecto.domain.model.note;

import jakarta.persistence.*;
import lombok.*;

/**
 * NoteLink - Enlaces entre notas (como Obsidian)
 * Patrón GRASP: Creador (Note es responsable de crear links)
 */
@Entity
@Table(name = "note_links")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_note_id", nullable = false)
    private Note linkedNote;

    private String relationshipType; // "MENTIONS", "REFERENCES", "RELATED"
}
