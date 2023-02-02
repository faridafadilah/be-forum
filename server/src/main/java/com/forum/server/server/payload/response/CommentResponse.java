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

  public static List<CommentResponse> getInstance(Thread thread) {
    if(thread != null) {
      try {
        List<CommentResponse> comments = new ArrayList<>();
        if(thread.getComments() != null) {
          if(!thread.getComments().isEmpty()) {
            for(Comment comment : thread.getComments()) {
              CommentResponse commentResponse = new CommentResponse();
              commentResponse.setId(comment.getId());
              commentResponse.setContent(comment.getContent());
              commentResponse.setThreadId(thread.getId());
              comments.add(commentResponse);
            }
          }
        }
        return comments;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
