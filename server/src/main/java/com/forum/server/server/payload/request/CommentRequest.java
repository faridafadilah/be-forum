package com.forum.server.server.payload.request;

import javax.validation.constraints.NotBlank;
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
public class CommentRequest {
  @NotBlank
  private String content;
  @NotBlank
  private Long threadId;
  @NotBlank
  private Long userId;
  @NotBlank
  private String nameUser;
  @NotBlank
  private String imageUser;
}
