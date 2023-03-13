package com.forum.server.server.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.forum.server.server.base.BaseSpecification;
import com.forum.server.server.models.Logs;

@Component
public class LogSpecification extends BaseSpecification<Logs> {
	@Override
	public Specification<Logs> containsTextInOmni(String text) {
		return containsTextInAttributes(text,
				Arrays.asList("method", "endpoint", "username"));
	}
}
