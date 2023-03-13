package com.forum.server.server.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import com.forum.server.server.models.*;

@Getter
@Setter
public class DtoResListLog {
  private Long id;
	private String method;
	private String endpoint;
	private Integer status;
	private String username;
	private Date createdAt;
	private String logSource;
	private String logMessage;

  public static DtoResListLog getInstance(Logs log) {
    DtoResListLog dto = new DtoResListLog();
    if(dto != null) {
      try {
        dto.setId(log.getId());
        dto.setMethod(log.getMethod());
        dto.setCreatedAt(log.getCreatedAt());
        dto.setUsername(log.getUsername());
        dto.setEndpoint(log.getEndpoint());
        dto.setStatus(log.getStatus());
        dto.setLogMessage(log.getLogMessage());
        dto.setLogSource(log.getLogSource());
        return dto;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
