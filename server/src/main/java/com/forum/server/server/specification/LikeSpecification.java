package com.forum.server.server.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.forum.server.server.models.UserLike;
import com.forum.server.server.models.UserLike_;
import com.forum.server.server.models.User;
import com.forum.server.server.models.User_;

@Component
public class LikeSpecification {
    public Specification<UserLike> userEqual(long id) {
        return (Specification<UserLike>) (root, cq, cb) -> {
            if (id == 0) {
                return null;
            } else {
                Join<UserLike, User> userJoin = root.join(UserLike_.USER);
                return cb.equal(userJoin.get(User_.ID), id);
            }
        };
    }
}
