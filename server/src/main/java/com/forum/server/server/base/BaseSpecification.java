package com.forum.server.server.base;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class BaseSpecification<T> {
  private Boolean caseSensitive = false;

    public Specification<T> containsTextAllInOmni(String text) {

        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = caseSensitive ? text : text.toLowerCase();

        return new Specification<T>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 6577374080474388953L;

			@Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder builder) {
                return builder.or(root.getModel().getDeclaredSingularAttributes().stream().filter(a -> {
                            if (a.getJavaType().getSimpleName().equalsIgnoreCase("string")) {
                                return true;
                            } else {
                                return false;
                            }
                        }).map(a -> builder.like(caseSensitive ? root.get(a.getName()) : builder.lower(root.get(a.getName())), finalText)
                        ).toArray(Predicate[]::new)
                );
            }
        };
    }

    public abstract Specification<T> containsTextInOmni(String text);
    public Specification<T> containsTextInAttributes(String text, List<String> attributes) {
      if (!text.contains("%")) {
          text = "%" + text + "%";
      }
      String finalText = caseSensitive ? text : text.toLowerCase();
      return (root, query, builder) -> builder.or(root.getModel().getDeclaredSingularAttributes().stream()
              .filter(a -> attributes.contains(a.getName()))
              .map(a -> builder.like(caseSensitive ? root.get(a.getName()) : builder.lower(root.get(a.getName())), finalText))
              .toArray(Predicate[]::new)
      );
  }
}

