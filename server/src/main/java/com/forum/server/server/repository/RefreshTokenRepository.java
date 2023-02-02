package com.forum.server.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.forum.server.server.models.RefreshToken;
import com.forum.server.server.models.User;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  
  @Modifying
  int deleteByUser(User user);

  @Query(value = "SELECT token_jwt FROM refreshtoken WHERE user_id= :userId", nativeQuery = true)
  String findTokenByUserId(@Param("userId") Long userId);

  // @Query(value = "SELECT * FROM refreshtoken WHERE user_id= :userId", nativeQuery = true)
  Optional<RefreshToken> findByUserId(Long userId);
}
