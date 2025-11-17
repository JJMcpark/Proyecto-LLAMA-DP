package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.domain.model.User;
import com.dpatrones.proyecto.domain.model.note.Note;
import com.dpatrones.proyecto.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * PATRÓN GRASP: Experto en Información sobre Notas
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    public Note createNote(User owner, String title, String content, String subject) {
        Note note = Note.builder()
                .owner(owner)
                .title(title)
                .content(content)
                .subject(subject)
                .build();
        return noteRepository.save(note);
    }

    public Note updateNote(Long noteId, String content) {
        return noteRepository.findById(noteId).map(n -> {
            n.setContent(content);
            return noteRepository.save(n);
        }).orElseThrow(() -> new com.dpatrones.proyecto.exception.ResourceNotFoundException("Nota no encontrada: " + noteId));
    }

    @Transactional(readOnly = true)
    public List<Note> getUserNotes(Long userId) {
        return noteRepository.findByOwnerId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
}
