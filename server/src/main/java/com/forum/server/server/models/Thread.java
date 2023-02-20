package com.forum.server.server.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Thread {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Title cannot be null")
  private String title;

  private String nameImage;
  private String urlImage;
  private Integer view = 0;
  private Integer liked = 0;

  @Column(columnDefinition = "TEXT")
  @NotNull(message = "Content cannot be null")
  private String content;

  @ManyToOne
  @JoinColumn(name = "subforum_id")
  private SubForum subForum;

  @OneToMany(mappedBy = "thread")
  private List<Comment> comments;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User users;
  
}
