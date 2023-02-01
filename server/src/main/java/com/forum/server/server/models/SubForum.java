package com.forum.server.server.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.forum.server.server.dto.request.SubRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubForum {
  public SubForum(SubRequest body) {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @NotNull(message = "Judul cannot be null")
  private String judul;

  @Column(columnDefinition = "TEXT")
  @NotNull(message = "Description cannot be null")
  private String description;
  
  @NotNull(message = "Image cannot be null")
  private String nameImage;

  @ManyToOne
  @JoinColumn(name = "main_id")
  private MainForum mainForum;

  @OneToMany(mappedBy = "subForum")
  private List<Thread> threads;
}
