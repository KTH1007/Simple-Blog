package org.example.springbootdeveloper.article.domain.repository;

import org.example.springbootdeveloper.article.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
