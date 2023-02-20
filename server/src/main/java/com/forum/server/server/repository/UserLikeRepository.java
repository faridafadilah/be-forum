package com.forum.server.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.forum.server.server.models.*;
import com.forum.server.server.models.Thread;

import java.util.*;

public interface UserLikeRepository extends JpaRepository<UserLike, Long>, JpaSpecificationExecutor<UserLike> {
  Optional<UserLike> findByThreadAndUser(Thread thread, User user);
  Optional<UserLike> findByUser(User user);
}
