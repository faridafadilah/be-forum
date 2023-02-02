package com.forum.server.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.forum.server.server.models.Logout;

@Repository
public interface LogoutRepository extends JpaRepository<Logout, String> {
  @Query(value = "SELECT * FROM logout WHERE token = :token", nativeQuery = true)
  Optional<Logout> findStatusByToken(@Param("token") String token);
}
