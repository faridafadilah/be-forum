package com.forum.server.server.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.forum.server.server.models.Thread;

import org.springframework.data.jpa.domain.Specification;

public class ThreadPopulerSpecification implements Specification<Thread> {

  @Override
    public Predicate toPredicate(Root<Thread> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.multiselect(
                root.get("id"),
                root.get("title"),
                root.get("content"),
                root.get("view"),
                root.get("liked"),
                criteriaBuilder.size(root.get("comments"))
        ).orderBy(
                criteriaBuilder.desc(
                        criteriaBuilder.sum(
                                criteriaBuilder.sum(root.get("view"), root.get("liked")),
                                criteriaBuilder.prod(criteriaBuilder.size(root.get("comments")), 10)
                        )
                )
        );
        
        return query.getRestriction();
    }
  
}