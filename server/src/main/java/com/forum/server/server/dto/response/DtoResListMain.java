package com.forum.server.server.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.forum.server.server.models.MainForum;
import com.forum.server.server.models.SubForum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoResListMain {
  private Long id;
  private String title;
  private String description;
  private String nameImage;
  public List<SubResponse> subForums;

  public static DtoResListMain getInstance(MainForum mainForum) {
    DtoResListMain dto = new DtoResListMain();
    if(mainForum != null) {
      try {
        dto.setId(mainForum.getId());
        dto.setTitle(mainForum.getTitle());
        dto.setDescription(mainForum.getDescription());
        dto.setNameImage(mainForum.getNameImage());
        
        List<SubResponse> subForums = new ArrayList<>();
        if(mainForum.getSubForums() != null) {
          if(!mainForum.getSubForums().isEmpty()) {
            for(SubForum subForum : mainForum.getSubForums()) {
              SubResponse subResponse = new SubResponse();
              subResponse.setId(subForum.getId());
              subResponse.setJudul(subForum.getJudul());
              subResponse.setDescription(subForum.getDescription());
              subResponse.setNameImage(subForum.getNameImage());
              subResponse.setMainForumId(mainForum.getId());
              subForums.add(subResponse);
            }
          }
        }
        dto.setSubForums(subForums);
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
