package io.hexlet.hexlet_spring_blog.controller;

import io.hexlet.hexlet_spring_blog.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    // Получить все посты
    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(new ArrayList<>(posts.values()));
    }

    // Получить пост по ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {
        Post post = posts.get(id);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(post);
    }

    // Создать пост
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Long id = nextId.getAndIncrement();

        post.setId(id);
        post.setCreatedAt(LocalDateTime.now());

        posts.put(id, post);

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    // Обновить пост
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(
            @PathVariable Long id,
            @RequestBody Post updatedPost
    ) {
        Post existingPost = posts.get(id);

        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setAuthor(updatedPost.getAuthor());

        return ResponseEntity.ok(existingPost);
    }

    // Удалить пост
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Post removed = posts.remove(id);

        if (removed == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}