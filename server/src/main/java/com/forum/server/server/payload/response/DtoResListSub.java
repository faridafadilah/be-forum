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
  public List<ThreadResponse> threads;

  public static DtoResListSub getInstance(SubForum subForum) {
    DtoResListSub dto = new DtoResListSub();
    if(subForum != null) {
      try {
        dto.setId(subForum.getId());
        dto.setJudul(subForum.getJudul());
        dto.setDescription(subForum.getDescription());
        dto.setNameImage(subForum.getNameImage());
        
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
