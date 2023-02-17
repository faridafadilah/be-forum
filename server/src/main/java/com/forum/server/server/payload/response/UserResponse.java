package com.forum.server.server.payload.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
  private String username;
  private String email;
  private String role;
  private String image;
  private String bio;
  private String github;
  private String whatsapp;
  private String linkedin;
  private String gender;
  private String address;
  private String hobies;
  private Date birth;
}
