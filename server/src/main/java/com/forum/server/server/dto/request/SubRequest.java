package com.forum.server.server.dto.request;

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
public class SubRequest {
  @NotBlank
  private String judul;
  @NotBlank
  private String description;
  @NotBlank
  private Long mainforumId;
}
