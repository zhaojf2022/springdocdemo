package com.zhaojf.springdocdemo.controller;

import java.util.ArrayList;
import java.util.List;

import com.zhaojf.springdocdemo.config.Constant;
import com.zhaojf.springdocdemo.exception.ResourceNotFoundException;
import com.zhaojf.springdocdemo.model.DocTag;
import com.zhaojf.springdocdemo.model.Todo;
import com.zhaojf.springdocdemo.repository.TagRepository;
import com.zhaojf.springdocdemo.repository.TodoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Tag接口", description = "TagAPI")
public class TagController {

  private final TodoRepository todoRepository;

  private final TagRepository tagRepository;

  @Autowired
  public TagController(TodoRepository todoRepository, TagRepository tagRepository) {
    this.todoRepository = todoRepository;
    this.tagRepository = tagRepository;
  }


  @GetMapping("/tags")
  public ResponseEntity<List<DocTag>> getAllTags() {

      List<DocTag> docTags = new ArrayList<>(tagRepository.findAll());

    if (docTags.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(docTags, HttpStatus.OK);
  }
  
  @GetMapping("/todos/{todoId}/tags")
  public ResponseEntity<List<DocTag>> getAllTagsByTodoId(@PathVariable(value = "todoId") Long todoId) {
    if (!todoRepository.existsById(todoId)) {
      throw new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId);
    }

    List<DocTag> docTags = tagRepository.findTagsByTodosId(todoId);
    return new ResponseEntity<>(docTags, HttpStatus.OK);
  }

  @GetMapping("/tags/{id}")
  public ResponseEntity<DocTag> getTagsById(@PathVariable(value = "id") Long id) {
    DocTag docTag = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found DocTag with id = " + id));

    return new ResponseEntity<>(docTag, HttpStatus.OK);
  }
  
  @GetMapping("/tags/{tagId}/todos")
  public ResponseEntity<List<Todo>> getAllTodosByTagId(@PathVariable(value = "tagId") Long tagId) {
    if (!tagRepository.existsById(tagId)) {
      throw new ResourceNotFoundException("Not found DocTag  with id = " + tagId);
    }

    List<Todo> todos = todoRepository.findTodosByTagsId(tagId);
    return new ResponseEntity<>(todos, HttpStatus.OK);
  }

  @PostMapping("/todos/{todoId}/tags")
  public ResponseEntity<DocTag> addTag(@PathVariable(value = "todoId") Long todoId, @RequestBody DocTag docTagRequest) {
    DocTag docTag = todoRepository.findById(todoId).map(todo -> {
      long tagId = docTagRequest.getId();
      
      // docTag is existed
      if (tagId != 0L) {
        DocTag innserDocTag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found DocTag with id = " + tagId));
        todo.addTag(innserDocTag);
        todoRepository.save(todo);
        return innserDocTag;
      }
      
      // add and create new DocTag
      todo.addTag(docTagRequest);
      return tagRepository.save(docTagRequest);
    }).orElseThrow(() -> new ResourceNotFoundException(Constant.TODO_NOT_FOUND + todoId));

    return new ResponseEntity<>(docTag, HttpStatus.CREATED);
  }

  @PutMapping("/tags/{id}")
  public ResponseEntity<DocTag> updateTag(@PathVariable("id") long id, @RequestBody DocTag docTagRequest) {
    DocTag docTag = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("TagId " + id + "not found"));

    docTag.setName(docTagRequest.getName());

    return new ResponseEntity<>(tagRepository.save(docTag), HttpStatus.OK);
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
