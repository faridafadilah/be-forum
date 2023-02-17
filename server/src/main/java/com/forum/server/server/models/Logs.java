package com.forum.server.server.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Logs {
	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long eventId;
	
	private String level;
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventDate; 

	@Column(columnDefinition = "TEXT")
	private String throwable; 
	 

	@Column(columnDefinition = "TEXT")
	private String message; 

	@Column(columnDefinition = "TEXT")
	private String logger;
}