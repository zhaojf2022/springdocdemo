package com.zhaojf.springdocdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhaojf.springdocdemo.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
  List<Tag> findTagsByTodosId(Long todoId);
}
