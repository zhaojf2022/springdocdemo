package com.zhaojf.springdocdemo.controller;

import java.util.ArrayList;
import java.util.List;

import com.zhaojf.springdocdemo.config.Constant;
import com.zhaojf.springdocdemo.exception.ResourceNotFoundException;
import com.zhaojf.springdocdemo.repository.TagRepository;
import com.zhaojf.springdocdemo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zhaojf.springdocdemo.model.Tag;
import com.zhaojf.springdocdemo.model.Todo;

@CrossOrigin(origins = "http://localhost:8999")
@RestController
@RequestMapping("/api")
public class TagController {

  private final TodoRepository todoRepository;

  private final TagRepository tagRepository;

  @Autowired
  public TagController(TodoRepository todoRepository, TagRepository tagRepository) {
    this.todoRepository = todoRepository;
    this.tagRepository = tagRepository;
  }


  @GetMapping("/tags")
  public ResponseEntity<List<Tag>> getAllTags() {

      List<Tag> tags = new ArrayList<>(tagRepository.findAll());

    if (tags.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(tags, HttpStatus.OK);
  }
  
  @GetMapping("/todos/{todoId}/tags")
  public ResponseEntity<List<Tag>> getAllTagsByTodoId(@PathVariable(value = "todoId") Long todoId) {
    if (!todoRepository.existsById(todoId)) {
      throw new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId);
    }

    List<Tag> tags = tagRepository.findTagsByTodosId(todoId);
    return new ResponseEntity<>(tags, HttpStatus.OK);
  }

  @GetMapping("/tags/{id}")
  public ResponseEntity<Tag> getTagsById(@PathVariable(value = "id") Long id) {
    Tag tag = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id = " + id));

    return new ResponseEntity<>(tag, HttpStatus.OK);
  }
  
  @GetMapping("/tags/{tagId}/todos")
  public ResponseEntity<List<Todo>> getAllTodosByTagId(@PathVariable(value = "tagId") Long tagId) {
    if (!tagRepository.existsById(tagId)) {
      throw new ResourceNotFoundException("Not found Tag  with id = " + tagId);
    }

    List<Todo> todos = todoRepository.findTodosByTagsId(tagId);
    return new ResponseEntity<>(todos, HttpStatus.OK);
  }

  @PostMapping("/todos/{todoId}/tags")
  public ResponseEntity<Tag> addTag(@PathVariable(value = "todoId") Long todoId, @RequestBody Tag tagRequest) {
    Tag tag = todoRepository.findById(todoId).map(todo -> {
      long tagId = tagRequest.getId();
      
      // tag is existed
      if (tagId != 0L) {
        Tag innserTag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id = " + tagId));
        todo.addTag(innserTag);
        todoRepository.save(todo);
        return innserTag;
      }
      
      // add and create new Tag
      todo.addTag(tagRequest);
      return tagRepository.save(tagRequest);
    }).orElseThrow(() -> new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId));

    return new ResponseEntity<>(tag, HttpStatus.CREATED);
  }

  @PutMapping("/tags/{id}")
  public ResponseEntity<Tag> updateTag(@PathVariable("id") long id, @RequestBody Tag tagRequest) {
    Tag tag = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("TagId " + id + "not found"));

    tag.setName(tagRequest.getName());

    return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.OK);
  }
 
  @DeleteMapping("/todos/{todoId}/tags/{tagId}")
  public ResponseEntity<HttpStatus> deleteTagFromTodo(@PathVariable(value = "todoId") Long todoId, @PathVariable(value = "tagId") Long tagId) {
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId));
    
    todo.removeTag(tagId);
    todoRepository.save(todo);
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  
  @DeleteMapping("/tags/{id}")
  public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") long id) {
    tagRepository.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
