package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.design.factory.PostFactory;
import com.dpatrones.proyecto.design.observer.PostEventManager;
import com.dpatrones.proyecto.design.singleton.ConfigService;
import com.dpatrones.proyecto.domain.model.User;
import com.dpatrones.proyecto.domain.model.post.Post;
import com.dpatrones.proyecto.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * PATRÓN GRASP: Experto en Información sobre Posts
 * PATRÓN GRASP: Controlador (orquesta operaciones)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostEventManager eventManager;
    private final ConfigService configService;

    public Post createBlogPost(User author, String title, String content) {
        if (!configService.isValidPostLength(content)) {
            throw new IllegalArgumentException("Post excede el tamaño máximo");
        }
        Post post = PostFactory.createBlogPost(author, title, content);
        return postRepository.save(post);
    }

    public Post createQuestionPost(User author, String title, String content) {
        Post post = PostFactory.createQuestionPost(author, title, content);
        return postRepository.save(post);
    }

    public Post publishPost(Long postId) {
        return postRepository.findById(postId).map(p -> {
            p.setStatus(Post.PostStatus.PUBLISHED);
            // notify subscribers that post was published
            eventManager.notifyCommentAdded(p.getTitle(), "Sistema");
            return postRepository.save(p);
        }).orElseThrow(() -> new com.dpatrones.proyecto.exception.ResourceNotFoundException("Post no encontrado: " + postId));
    }

    public Post archivePost(Long postId) {
        return postRepository.findById(postId).map(p -> {
            p.setStatus(Post.PostStatus.ARCHIVED);
            return postRepository.save(p);
        }).orElseThrow(() -> new com.dpatrones.proyecto.exception.ResourceNotFoundException("Post no encontrado: " + postId));
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPublishedPosts() {
        return postRepository.findByStatus(Post.PostStatus.PUBLISHED);
    }

    @Transactional(readOnly = true)
    public List<Post> getUserPosts(Long userId) {
        return postRepository.findByAuthorId(userId);
    }
}
