package com.forum.server.server.models;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Logs {
	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

	private String method;
	private String endpoint;
	private Integer status;
	private String username;
	private Date createdAt;
	private String logSource;
	private String logMessage;
}