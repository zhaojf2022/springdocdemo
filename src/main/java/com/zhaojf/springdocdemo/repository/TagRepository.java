package com.zhaojf.springdocdemo.repository;

import java.util.List;

import com.zhaojf.springdocdemo.model.DocTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<DocTag, Long> {
  List<DocTag> findTagsByTodosId(Long todoId);
}
