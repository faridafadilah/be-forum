package com.forum.server.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forum.server.server.models.MainForum;

public interface MainForumRepository extends JpaRepository<MainForum, Long>, JpaSpecificationExecutor<MainForum> {
  
}
