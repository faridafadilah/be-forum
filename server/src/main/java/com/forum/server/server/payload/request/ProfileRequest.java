package com.forum.server.server.payload.request;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class ProfileRequest {
  private String username;
  private String email;
  private String image;
  private String bio;
  private String github;
  private String whatsapp;
  private String linkedin;
  private String gender;
  private String address;
  private String hobies;
  private String birth;
}
