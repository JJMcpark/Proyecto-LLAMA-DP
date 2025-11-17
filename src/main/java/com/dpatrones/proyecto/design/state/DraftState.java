package com.dpatrones.proyecto.design.state;

import org.springframework.stereotype.Service;

import com.dpatrones.proyecto.domain.model.post.Post;
import com.dpatrones.proyecto.domain.model.post.Post.PostStatus;

/**
 * PATRÓN STATE - Estado DRAFT
 * Post en borrador, puede ser editado o publicado
 */
@Service
public class DraftState implements PostState {
    
    @Override
    public void publish(Post post) {
        post.setStatus(PostStatus.PUBLISHED);
        System.out.println("Post publicado desde DRAFT");
    }
    
    @Override
    public void archive(Post post) {
        System.out.println("No se puede archivar un borrador");
    }
    
    @Override
    public void draft(Post post) {
        System.out.println("Post ya está en DRAFT");
    }
    
    @Override
    public String getStatus() {
        return "DRAFT";
    }
}
