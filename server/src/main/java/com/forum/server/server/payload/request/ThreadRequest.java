package com.forum.server.server.payload.request;

import javax.validation.constraints.NotNull;

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
public class ThreadRequest {
  @NotNull
  private String title;
  @NotNull
  private String content;
  @NotNull
  private Long subforumId;
}
