package com.zhaojf.springdocdemo.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhaojf.springdocdemo.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByTodoId(Long postId);
  
  @Transactional
  void deleteByTodoId(long todoId);
}
