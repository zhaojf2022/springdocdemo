package com.zhaojf.springdocdemo.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "todo_details")
public class TodoDetails {
  @Id
  private Long id;

  @Column
  private Date createdOn;

  @Column
  private String createdBy;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "todo_id")
  private Todo todo;

  public TodoDetails() {
  }

  public TodoDetails(String createdBy) {
    this.createdOn = new Date();
    this.createdBy = createdBy;
  }

}
