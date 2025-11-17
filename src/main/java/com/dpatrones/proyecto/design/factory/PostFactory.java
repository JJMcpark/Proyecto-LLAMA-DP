package com.dpatrones.proyecto.design.factory;

import com.dpatrones.proyecto.domain.model.User;
import com.dpatrones.proyecto.domain.model.post.Post;

/**
 * PATRÓN FACTORY
 * Crea diferentes tipos de Posts (Blog, Question, Resource)
 */
public class PostFactory {
    
    public static Post createBlogPost(User author, String title, String content) {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .postType("BLOG")
                .build();
    }
    
    public static Post createQuestionPost(User author, String title, String content) {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .postType("QUESTION")
                .build();
    }
    
    public static Post createResourcePost(User author, String title, String content) {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .postType("RESOURCE")
                .build();
    }
    
    public static Post createPost(User author, String title, String content, String type) {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .postType(type)
                .build();
    }
}
