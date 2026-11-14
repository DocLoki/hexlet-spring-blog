
package io.hexlet.hexlet_spring_blog.repository;

import io.hexlet.hexlet_spring_blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
