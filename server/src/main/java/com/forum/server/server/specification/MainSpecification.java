package com.forum.server.server.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.forum.server.server.base.BaseSpecification;
import com.forum.server.server.models.MainForum;

@Component
public class MainSpecification extends BaseSpecification<MainForum> {
	@Override
	public Specification<MainForum> containsTextInOmni(String text) {
		return containsTextInAttributes(text,
				Arrays.asList("description", "title"));
	}
}
