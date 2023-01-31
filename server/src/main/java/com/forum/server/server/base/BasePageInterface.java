package com.forum.server.server.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


public interface BasePageInterface<T, S extends BaseSpecification<T>, R, ID> {
  default Pageable defaultPage(String search, Integer page, Integer limit, List<String> sortBy, Boolean desc) {
    page = (page != null) ? page : 0;
    limit = (limit != null) ? limit: 5;
    desc = (desc != null) ? desc: Boolean.FALSE;
    search = (search != null) ? search : "";

    List<Sort.Order> orders = new ArrayList<>();
    for (String rowSort:sortBy) {
      Sort.Order order = null;
      if(!desc) {
        order = new Sort.Order(Sort.Direction.ASC, rowSort);
      } else {
        order = new Sort.Order(Sort.Direction.DESC, rowSort);
      }
      orders.add(order);
    }
    Sort sort = Sort.by(orders);
    return PageRequest.of(page, limit, sort);
  }

  default Specification<T> defaultSpec(String search, S spec) {
    search = (search != null) ? search : "";
    return spec.containsTextInOmni(search);
  }
}
