package com.zhaojf.springdocdemo.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zhaojf.springdocdemo.model.TodoDetails;

@Repository
public interface TodoDetailsRepository extends JpaRepository<TodoDetails, Long> {
  @Transactional
  void deleteById(long id);
  
  @Transactional
  void deleteByTodoId(long todoId);
}
