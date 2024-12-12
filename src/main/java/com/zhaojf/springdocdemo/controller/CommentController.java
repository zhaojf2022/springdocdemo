package com.zhaojf.springdocdemo.controller;

import java.util.List;

import com.zhaojf.springdocdemo.config.Constant;
import com.zhaojf.springdocdemo.exception.ResourceNotFoundException;
import com.zhaojf.springdocdemo.repository.CommentRepository;
import com.zhaojf.springdocdemo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaojf.springdocdemo.model.Comment;

@CrossOrigin(origins = "http://localhost:8999")
@RestController
@RequestMapping("/api")
public class CommentController {

  private final TodoRepository todoRepository;

  private final CommentRepository commentRepository;

  @Autowired
  public CommentController(TodoRepository todoRepository, CommentRepository commentRepository){
    this.todoRepository = todoRepository;
    this.commentRepository = commentRepository;
    
  }

  @GetMapping("/todos/{todoId}/comments")
  public ResponseEntity<List<Comment>> getAllCommentsByTodoId(@PathVariable(value = "todoId") Long todoId) {
    if (!todoRepository.existsById(todoId)) {
      throw new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId);
    }

    List<Comment> comments = commentRepository.findByTodoId(todoId);
    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @GetMapping("/comments/{id}")
  public ResponseEntity<Comment> getCommentsByTodoId(@PathVariable(value = "id") Long id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));

    return new ResponseEntity<>(comment, HttpStatus.OK);
  }

  @PostMapping("/todos/{todoId}/comments")
  public ResponseEntity<Comment> createComment(@PathVariable(value = "todoId") Long todoId,
      @RequestBody Comment commentRequest) {
    Comment comment = todoRepository.findById(todoId).map(todo -> {
      commentRequest.setTodo(todo);
      return commentRepository.save(commentRequest);
    }).orElseThrow(() -> new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId));

    return new ResponseEntity<>(comment, HttpStatus.CREATED);
  }

  @PutMapping("/comments/{id}")
  public ResponseEntity<Comment> updateComment(@PathVariable("id") long id, @RequestBody Comment commentRequest) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + "not found"));

    comment.setContent(commentRequest.getContent());

    return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.OK);
  }

  @DeleteMapping("/comments/{id}")
  public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") long id) {
    commentRepository.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  
  @DeleteMapping("/todos/{todoId}/comments")
  public ResponseEntity<List<Comment>> deleteAllCommentsOfTodo(@PathVariable(value = "todoId") Long todoId) {
    if (!todoRepository.existsById(todoId)) {
      throw new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId);
    }

    commentRepository.deleteByTodoId(todoId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
