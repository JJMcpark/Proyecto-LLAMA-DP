package com.dpatrones.proyecto.design.state;

import org.springframework.stereotype.Service;

import com.dpatrones.proyecto.domain.model.post.Post;

/**
 * PATRÓN STATE
 * Interfaz para los estados del Post (Draft, Published, Archived)
 */
@Service
public interface PostState {
    void publish(Post post);
    void archive(Post post);
    void draft(Post post);
    String getStatus();
}
