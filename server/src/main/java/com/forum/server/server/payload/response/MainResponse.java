package com.forum.server.server.payload.response;

import lombok.Data;

@Data
public class MainResponse {
  private Long id;
  private String title;
  private String description;
  private String nameImage;
}
