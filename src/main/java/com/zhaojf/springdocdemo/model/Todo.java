package com.zhaojf.springdocdemo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="todo")
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String title;

	private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<DocTag> tags = new HashSet<>();

    public Todo() {}

    public Todo(String title, String description) {
        this.title=title;
        this.description=description;
    }

    public Todo(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }


    public void addTag(DocTag docTag) {
        this.tags.add(docTag);
        docTag.getTodos().add(this);
    }

    public void removeTag(long tagId) {
        DocTag docTag = this.tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
        if (docTag != null) {
            this.tags.remove(docTag);
            docTag.getTodos().remove(this);
        }
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + ", title=" + title + ", desc=" + description + "]";
    }


}
