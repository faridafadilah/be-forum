package com.forum.server.server.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.Join;

import com.forum.server.server.models.Comment;
import com.forum.server.server.models.Comment_;
import com.forum.server.server.models.Thread;
import com.forum.server.server.models.Thread_;

@Component
public class CommentSpecification {
  public Specification<Comment> threadEqual(long id) {
		return (Specification<Comment>) (root, cq, cb) -> {
				if (id == 0) {
						return null;
				} else {
						Join<Comment, Thread> threadJoin = root.join(Comment_.THREAD);
						return cb.equal(threadJoin.get(Thread_.ID), id);
				}
		};
}
}
