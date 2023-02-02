package com.forum.server.server.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.forum.server.server.models.Comment;
import com.forum.server.server.models.Thread;

@Getter
@Setter
public class DtoResListThread {
  private Long id;
  private String title;
  private String content;
  private String nameImage;
  public List<CommentResponse> comments;

  public static DtoResListThread getInstance(Thread thread) {
    DtoResListThread dto = new DtoResListThread();
    if(thread != null) {
      try {
        dto.setId(thread.getId());
        dto.setTitle(thread.getTitle());
        dto.setContent(thread.getContent());
        dto.setNameImage(thread.getNameImage());
        
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
        dto.setComments(comments);
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
