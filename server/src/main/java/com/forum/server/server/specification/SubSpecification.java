package com.forum.server.server.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.forum.server.server.models.MainForum;
import com.forum.server.server.models.MainForum_;
import com.forum.server.server.models.SubForum;
import com.forum.server.server.models.SubForum_;

@Component
public class SubSpecification {
    public Specification<SubForum> mainEqual(long id) {
        return (Specification<SubForum>) (root, cq, cb) -> {
            if (id == 0) {
                return null;
            } else {
                Join<SubForum, MainForum> mainJoin = root.join(SubForum_.MAIN_FORUM);
                return cb.equal(mainJoin.get(MainForum_.ID), id);
            }
        };
    }
}
