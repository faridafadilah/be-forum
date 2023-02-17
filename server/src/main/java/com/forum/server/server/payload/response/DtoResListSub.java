package com.forum.server.server.payload.response;

import java.util.ArrayList;
import java.util.List;

import com.forum.server.server.models.SubForum;
import com.forum.server.server.models.Thread;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoResListSub {
  private Long id;
  private String judul;
  private String description;
  private String nameImage;
  private String urlImage;
  private Long mainforumId;
  public List<ThreadResponse> threads;

  public static DtoResListSub getInstance(SubForum subForum) {
    DtoResListSub dto = new DtoResListSub();
    if(subForum != null) {
      try {
        dto.setId(subForum.getId());
        dto.setJudul(subForum.getJudul());
        dto.setDescription(subForum.getDescription());
        dto.setNameImage(subForum.getNameImage());
        dto.setUrlImage(subForum.getUrlImage());
        dto.setMainforumId(subForum.getMainForum().getId());
        
        List<ThreadResponse> threads = new ArrayList<>();
        if(subForum.getThreads() != null) {
          if(!subForum.getThreads().isEmpty()) {
            for(Thread thread : subForum.getThreads()) {
              ThreadResponse threadResponse = new ThreadResponse();
              threadResponse.setId(thread.getId());
              threadResponse.setTitle(thread.getTitle());
              threadResponse.setContent(thread.getContent());
              threadResponse.setNameImage(thread.getNameImage());
              threadResponse.setSubforumId(subForum.getId());
              threadResponse.setUserId(thread.getUsers().getId());
              threadResponse.setUsername(thread.getUsers().getUsername());
              threadResponse.setImage(thread.getUsers().getImage());
              threadResponse.setUrlImage(thread.getUrlImage());
              threadResponse.setUrlUser(thread.getUsers().getUrlImage());
              threads.add(threadResponse);
            }
          }
        }
        dto.setThreads(threads);
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
