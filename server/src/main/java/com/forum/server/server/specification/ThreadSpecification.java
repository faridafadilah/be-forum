package com.forum.server.server.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.forum.server.server.models.Thread_;
import com.forum.server.server.models.User;
import com.forum.server.server.models.Thread;
import com.forum.server.server.models.SubForum;
import com.forum.server.server.models.SubForum_;
import com.forum.server.server.models.User_;

@Component
public class ThreadSpecification {
  public Specification<Thread> subEqual(long id) {
    return (Specification<Thread>) (root, cq, cb) -> {
      if (id == 0) {
        return null;
      } else {
        Join<Thread, SubForum> subJoin = root.join(Thread_.SUB_FORUM);
        return cb.equal(subJoin.get(SubForum_.ID), id);
      }
    };
  }

  public Specification<Thread> userEqual(long id) {
    return (Specification<Thread>) (root, cq, cb) -> {
      if (id == 0) {
        return null;
      } else {
        Join<Thread, User> userJoin = root.join(Thread_.USERS);
        return cb.equal(userJoin.get(User_.ID), id);
      }
    };
  }
}
