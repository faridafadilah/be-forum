package com.forum.server.server.models;

import java.time.Instant;
import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "refreshtoken")
@Getter
@Setter
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private String tokenJwt;

  private Date created_at;

  @Column(nullable = false)
  private Instant expiryDate;

  @Column(nullable = false)
  private boolean status;

  public RefreshToken() {
  }

}


