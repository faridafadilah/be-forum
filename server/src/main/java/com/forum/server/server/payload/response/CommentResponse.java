package com.forum.server.server.payload.response;

import java.util.ArrayList;

import com.forum.server.server.models.Comment;
import com.forum.server.server.models.Thread;
import java.util.List;

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
public class CommentResponse {
  private Long id;
  private String content;
  private Long threadId;
  private Long userId;
  private String username;
  private String imageUser;
  private String urlUser;

  public static CommentResponse getInstance(Comment comment) {
    CommentResponse dto = new CommentResponse();
    if(dto != null) {
      try {
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setThreadId(comment.getThread().getId());
        dto.setUserId(comment.getUsers().getId());
        dto.setUsername(comment.getUsers().getUsername());
        dto.setImageUser(comment.getUsers().getImage());
        dto.setUrlUser(comment.getUsers().getUrlImage());
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
