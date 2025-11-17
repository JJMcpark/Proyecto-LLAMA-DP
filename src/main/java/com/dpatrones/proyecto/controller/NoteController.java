package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.domain.model.User;
import com.dpatrones.proyecto.domain.model.note.Note;
import com.dpatrones.proyecto.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller para Notes
 * GRASP: NoteService es experto en operaciones de notas
 */
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody NoteCreateRequest request) {
        User owner = new User();
        owner.setId(request.getOwnerId());
        
        Note note = noteService.createNote(owner, request.getTitle(), 
                                          request.getContent(), request.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long id,
            @RequestBody NoteUpdateRequest request) {
        Note note = noteService.updateNote(id, request.getContent());
        return ResponseEntity.ok(note);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Note>> getUserNotes(@PathVariable Long userId) {
        List<Note> notes = noteService.getUserNotes(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteService.getNoteById(id);
        return note.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    static class NoteCreateRequest {
        public Long ownerId;
        public String title;
        public String content;
        public String subject;

        public Long getOwnerId() { return ownerId; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getSubject() { return subject; }
    }

    static class NoteUpdateRequest {
        public String content;
        public String getContent() { return content; }
    }
}
