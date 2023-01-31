package com.forum.server.server.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.forum.server.server.models.SubForum;
import com.forum.server.server.models.Thread;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ThreadResponse {
  private Long id;
  private String title;
  private String content;
  private Long subforumId;

  public static List<ThreadResponse> getInstance(SubForum subForum) {
    if(subForum != null) {
      try {
        List<ThreadResponse> threads = new ArrayList<>();
        if(subForum.getThreads() != null) {
          if(!subForum.getThreads().isEmpty()) {
            for(Thread thread : subForum.getThreads()) {
              ThreadResponse threadResponse = new ThreadResponse();
              threadResponse.setId(thread.getId());
              threadResponse.setTitle(thread.getTitle());
              threadResponse.setContent(thread.getContent());
              threadResponse.setSubforumId(subForum.getId());
              threads.add(threadResponse);
            }
          }
        }
        return threads;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
