package com.forum.server.server.payload.response;

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
  private String nameImage;
  private String urlImage;
  private Long subforumId;
  private Long userId;
  private String username;
  private String image;
  private String urlUser;
  private int liked;
  private int view;

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
              threadResponse.setNameImage(thread.getNameImage());
              threadResponse.setUrlImage(thread.getUrlImage());
              threadResponse.setSubforumId(subForum.getId());
              threadResponse.setUserId(thread.getUsers().getId());
              threadResponse.setUsername(thread.getUsers().getUsername());
              threadResponse.setImage(thread.getUsers().getImage());
              threadResponse.setUrlUser(thread.getUsers().getUrlImage());
              threadResponse.setLiked(thread.getLiked());
              threadResponse.setView(thread.getView());
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
