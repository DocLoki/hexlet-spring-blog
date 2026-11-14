
package io.hexlet.hexlet_spring_blog.controller;

import io.hexlet.hexlet_spring_blog.model.Post;
import io.hexlet.hexlet_spring_blog.model.User;
import io.hexlet.hexlet_spring_blog.repository.PostRepository;
import io.hexlet.hexlet_spring_blog.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping({"/posts", "/api/posts"})
public class PostsController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostsController(
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        post.setId(null);

        if (post.getCreatedAt() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }

        if (post.getUser() != null && post.getUser().getId() != null) {
            User user = userRepository.findById(post.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            post.setUser(user);
        }

        Post savedPost = postRepository.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(
            @PathVariable Long id,
            @RequestBody Post updatedPost
    ) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(updatedPost.getTitle());
                    post.setContent(updatedPost.getContent());
                    post.setAuthor(updatedPost.getAuthor());
                    post.setPublished(updatedPost.isPublished());

                    if (updatedPost.getUser() != null
                            && updatedPost.getUser().getId() != null) {
                        User user = userRepository.findById(updatedPost.getUser().getId())
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                        post.setUser(user);
                    }

                    return ResponseEntity.ok(postRepository.save(post));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
