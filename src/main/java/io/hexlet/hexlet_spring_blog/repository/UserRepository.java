
package io.hexlet.hexlet_spring_blog.repository;

import io.hexlet.hexlet_spring_blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
