package com.forum.server.server.specification;

import java.util.Arrays;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.forum.server.server.base.BaseSpecification;
import com.forum.server.server.models.Thread;

@Component
public class ThreadSpecification extends BaseSpecification<Thread> {
	@Override
	public Specification<Thread> containsTextInOmni(String text) {
		return containsTextInAttributes(text,
				Arrays.asList("content", "title"));
	}
}
