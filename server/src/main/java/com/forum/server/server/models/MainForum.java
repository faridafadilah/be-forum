package com.forum.server.server.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.metamodel.SingularAttribute;
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
public class MainForum {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "Title cannot be null")
  private String title;

  @NotNull(message = "Image cannot be null")
  private String nameImage;
  
  private String urlImage;

  @Column(columnDefinition = "TEXT")
  @NotNull(message = "Description cannot be null")
  private String description;

  @OneToMany(mappedBy = "mainForum")
  private List<SubForum> subForums;

}
