package com.forum.server.server.specification;

import java.util.Arrays;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.forum.server.server.base.BaseSpecification;
import com.forum.server.server.models.SubForum;

@Component
public class SubSpecification extends BaseSpecification<SubForum> {
  @Override
	public Specification<SubForum> containsTextInOmni(String text) {
		return containsTextInAttributes(text,
				Arrays.asList("description", "judul"));
	}
}
