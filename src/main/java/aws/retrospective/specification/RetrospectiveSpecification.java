package aws.retrospective.specification;

import aws.retrospective.entity.Retrospective;
import org.springframework.data.jpa.domain.Specification;

public class RetrospectiveSpecification {


    public static Specification<Retrospective> withKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),
            "%" + keyword + "%");
    }

}
