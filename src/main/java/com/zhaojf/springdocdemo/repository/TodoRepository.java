package com.zhaojf.springdocdemo.repository;

import java.util.List;

import com.zhaojf.springdocdemo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByTitleContaining(String title);
    List<Todo> findTodosByTagsId(Long tagId);

}
