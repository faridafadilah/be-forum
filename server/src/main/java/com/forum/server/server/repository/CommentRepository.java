package com.forum.server.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forum.server.server.models.Comment;

public interface CommentRepository<T> extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<T> {
  
}