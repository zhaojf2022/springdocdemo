package com.zhaojf.springdocdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.zhaojf.springdocdemo.model.Todo;
import com.zhaojf.springdocdemo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Todo接口",description="Todo 管理API")
@RestController
@RequestMapping("/api")
public class TodoController {
    
    TodoRepository todoRepository;

	@Autowired
	public TodoController(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Operation(summary = "Retrieve all Todo", tags = { "todos", "get", "filter" })
	@ApiResponse(responseCode = "200", content = {
		@Content(schema = @Schema(implementation = Todo.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "204", description = "There are no Todo", content = {
		@Content(schema = @Schema()) })
	@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getAllTodos(@RequestParam(required = false) String title) {
        try {
            List<Todo> todos = new ArrayList<>();

            if (title==null)
                todos.addAll(todoRepository.findAll());
            else
                todos.addAll(todoRepository.findByTitleContaining(title));

            if (todos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(todos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

	@Operation(
		summary = "Retrieve a Todo by Id",
		description = "Get a Todo object by specifying its id. The response is Todo object with id, title, description and status.",
		tags = { "todos", "get" })
	@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Todo.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
	@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    @GetMapping("/todos/{id}")
	public ResponseEntity<Todo> getTodoById(@PathVariable("id") long id) {
		Optional<Todo> todoData = todoRepository.findById(id);

        return todoData.map(todo -> new ResponseEntity<>(todo, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@Operation(summary = "Create a new Todo", tags = { "todo", "post" })
	@ApiResponse(responseCode = "201", content = {
		@Content(schema = @Schema(implementation = Todo.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	@PostMapping("/todos")
	public ResponseEntity<Todo> createTutorial(@RequestBody Todo todo) {
		try {
			Todo innerTodo = todoRepository
					.save(new Todo(todo.getTitle(), todo.getDescription()));
			return new ResponseEntity<>(innerTodo, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Update a Todo by Id", tags = { "todo", "put" })
	@ApiResponse(responseCode = "200", content = {
		@Content(schema = @Schema(implementation = Todo.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
	@PutMapping("/todos/{id}")
	public ResponseEntity<Todo> updateTutorial(@PathVariable("id") long id, @RequestBody Todo todo) {
		Optional<Todo> todoData = todoRepository.findById(id);

		if (todoData.isPresent()) {
			Todo innerTodo = todoData.get();
			innerTodo.setTitle(todo.getTitle());
			innerTodo.setDescription(todo.getDescription());
			return new ResponseEntity<>(todoRepository.save(innerTodo), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Delete a Todo by Id", tags = { "todos", "delete" })
	@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) })
	@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	@DeleteMapping("/todos/{id}")
	public ResponseEntity<HttpStatus> deleteTodo(@PathVariable("id") long id) {
		try {
			todoRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Delete all Todo", tags = { "todos", "delete" })
	@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) })
	@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	@DeleteMapping("/todos")
	public ResponseEntity<HttpStatus> deleteAllTodos() {
		try {
			todoRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
