package com.forum.server.server.payload.response;

import java.util.*;

import com.forum.server.server.models.Role;
import com.forum.server.server.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoResProfile {
  private Long id;
  private String username;
  private String email;
  private String image;
  private String urlImage;
  private String bio;
  private String github;
  private String whatsapp;
  private String linkedin;
  private String gender;
  private String address;
  private String hobies;
  private String birth;

  public static DtoResProfile getInstance(User user) {
    DtoResProfile dto = new DtoResProfile();
    if(user != null) {
      try {
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setImage(user.getImage());
        dto.setUrlImage(user.getUrlImage());
        dto.setBio(user.getBio());
        dto.setGithub(user.getGithub());
        dto.setWhatsapp(user.getWhatsapp());
        dto.setLinkedin(user.getLinkedin());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setHobies(user.getHobies());
        dto.setBirth(user.getBirth());
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
