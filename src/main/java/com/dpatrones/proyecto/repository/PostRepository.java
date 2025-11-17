package com.dpatrones.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpatrones.proyecto.domain.model.post.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStatus(Post.PostStatus status);
    List<Post> findByAuthorId(Long authorId);
}
