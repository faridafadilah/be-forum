package com.forum.server.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forum.server.server.models.SubForum;

public interface SubForumRepository extends JpaRepository<SubForum, Long>, JpaSpecificationExecutor<SubForum> {
  
}
