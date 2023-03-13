package com.forum.server.server.payload.response;

import com.forum.server.server.models.UserLike;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponse {
  private Long threadId;
  private String nameThread;
  private Long userId;
  private String nameUser;

  public static LikeResponse getInstance(UserLike like) {
    LikeResponse dto = new LikeResponse();
    if(dto != null) {
      try {
        dto.setThreadId(like.getThread().getId());
        dto.setUserId(like.getUser().getId());
        dto.setNameUser(like.getUser().getUsername());
        dto.setNameThread(like.getThread().getTitle());
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
