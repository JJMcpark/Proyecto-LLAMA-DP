package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.domain.model.User;
import com.dpatrones.proyecto.domain.model.post.Comment;
import com.dpatrones.proyecto.domain.model.post.Post;
import com.dpatrones.proyecto.repository.CommentRepository;
import com.dpatrones.proyecto.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para Comments
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentCreateRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post no encontrado"));
        
        User author = new User();
        author.setId(request.getAuthorId());
        
        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .content(request.getContent())
                .build();
        
        Comment saved = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    static class CommentCreateRequest {
        public Long postId;
        public Long authorId;
        public String content;

        public Long getPostId() { return postId; }
        public Long getAuthorId() { return authorId; }
        public String getContent() { return content; }
    }
}
