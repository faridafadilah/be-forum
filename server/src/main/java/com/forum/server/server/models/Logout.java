package com.forum.server.server.models;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@IdClass(LogoutId.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Logout {
  @Id
  private String token;

  @Id
  private String username;
  private Date created_at;
  private Instant expired_at;
  private boolean is_logged_out;
}
