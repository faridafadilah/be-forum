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
  private String urlImage;
  private Long subforumId;
  public List<CommentResponse> comments;
  private Long userId;
  private String username;
  private int liked;
  private int view;
  private String image;
  private String urlUser;

  public static DtoResListThread getInstance(Thread thread) {
    DtoResListThread dto = new DtoResListThread();
    if(thread != null) {
      try {
        dto.setId(thread.getId());
        dto.setTitle(thread.getTitle());
        dto.setContent(thread.getContent());
        dto.setNameImage(thread.getNameImage());
        dto.setUrlImage(thread.getUrlImage());
        dto.setUrlUser(thread.getUsers().getUrlImage());
        dto.setSubforumId(thread.getSubForum().getId());
        dto.setView(thread.getView());
        dto.setLiked(thread.getLiked());
        
        List<CommentResponse> comments = new ArrayList<>();
        if(thread.getComments() != null) {
          if(!thread.getComments().isEmpty()) {
            for(Comment comment : thread.getComments()) {
              CommentResponse commentResponse = new CommentResponse();
              commentResponse.setId(comment.getId());
              commentResponse.setContent(comment.getContent());
              commentResponse.setThreadId(thread.getId());
              commentResponse.setUserId(comment.getUsers().getId());
              commentResponse.setUsername(comment.getUsers().getUsername());
              commentResponse.setImageUser(comment.getUsers().getImage());
              comments.add(commentResponse);
            }
          }
        }

        dto.setUserId(thread.getUsers().getId());
        dto.setUsername(thread.getUsers().getUsername());
        dto.setImage(thread.getUsers().getImage());
        dto.setComments(comments);
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
