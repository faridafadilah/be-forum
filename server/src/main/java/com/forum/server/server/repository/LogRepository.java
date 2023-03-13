package com.forum.server.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.forum.server.server.models.Logs;

public interface LogRepository extends JpaRepository<Logs, Long>, JpaSpecificationExecutor<Logs> {
  
}
