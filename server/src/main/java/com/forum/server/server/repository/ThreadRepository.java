package com.forum.server.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.forum.server.server.models.Thread;

public interface ThreadRepository extends JpaRepository<Thread, Long>, JpaSpecificationExecutor<Thread> {
  
}
