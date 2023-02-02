package com.forum.server.server.payload.response;

import java.util.ArrayList;
import java.util.List;

import com.forum.server.server.models.MainForum;
import com.forum.server.server.models.SubForum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubResponse {
  private Long id;
  private String judul;
  private String description;
  private String nameImage;
  private Long mainForumId;

  public static List<SubResponse> getInstance(MainForum mainForum) {
    if(mainForum != null) {
      try {
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
        return subForums;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
