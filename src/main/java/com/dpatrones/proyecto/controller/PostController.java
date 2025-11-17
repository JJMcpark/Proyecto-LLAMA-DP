package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.domain.model.User;
import com.dpatrones.proyecto.domain.model.post.Post;
import com.dpatrones.proyecto.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para Posts
 * Usa FACTORY (PostFactory) y SINGLETON (ConfigService)
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/blog")
    public ResponseEntity<Post> createBlogPost(
            @RequestBody PostCreateRequest request) {
        User author = new User();
        author.setId(request.getAuthorId());
        
        Post post = postService.createBlogPost(author, request.getTitle(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PostMapping("/question")
    public ResponseEntity<Post> createQuestionPost(
            @RequestBody PostCreateRequest request) {
        User author = new User();
        author.setId(request.getAuthorId());
        
        Post post = postService.createQuestionPost(author, request.getTitle(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Post> publishPost(@PathVariable Long id) {
        Post post = postService.publishPost(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Post> archivePost(@PathVariable Long id) {
        Post post = postService.archivePost(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/published")
    public ResponseEntity<List<Post>> getPublishedPosts() {
        List<Post> posts = postService.getAllPublishedPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Long userId) {
        List<Post> posts = postService.getUserPosts(userId);
        return ResponseEntity.ok(posts);
    }

    static class PostCreateRequest {
        public Long authorId;
        public String title;
        public String content;

        public Long getAuthorId() { return authorId; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
    }
}
