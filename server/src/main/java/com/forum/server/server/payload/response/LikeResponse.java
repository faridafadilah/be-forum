package com.forum.server.server.payload.response;

import com.forum.server.server.models.UserLike;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponse {
  private Long threadId;
  private Long userId;

  public static LikeResponse getInstance(UserLike like) {
    LikeResponse dto = new LikeResponse();
    if(dto != null) {
      try {
        dto.setThreadId(like.getThread().getId());
        dto.setUserId(like.getUser().getId());
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
