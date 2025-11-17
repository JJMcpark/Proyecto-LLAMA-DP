package com.dpatrones.proyecto.design.state;

import org.springframework.stereotype.Service;

import com.dpatrones.proyecto.domain.model.post.Post;
import com.dpatrones.proyecto.domain.model.post.Post.PostStatus;

/**
 * PATRÓN STATE - Estado PUBLISHED
 * Post publicado, puede ser archivado pero no editado
 */
@Service
public class PublishedState implements PostState {
    
    @Override
    public void publish(Post post) {
        System.out.println("Post ya está publicado");
    }
    
    @Override
    public void archive(Post post) {
        post.setStatus(PostStatus.ARCHIVED);
        System.out.println("Post archivado desde PUBLISHED");
    }
    
    @Override
    public void draft(Post post) {
        System.out.println("No se puede pasar a DRAFT un post publicado");
    }
    
    @Override
    public String getStatus() {
        return "PUBLISHED";
    }
}
