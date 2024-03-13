package aws.retrospective.specification;

import aws.retrospective.entity.Retrospective;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RetrospectiveSpecification {

    public static Specification<Retrospective> withUserId(Long userId) {
        return (root, query, cb) -> {
            Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
            Predicate teamPredicate = cb.equal(root.get("team").get("users").get("id"), userId);
            return cb.or(userPredicate, teamPredicate);
        };
    }


    public static Specification<Retrospective> withKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return (root, query, cb) -> cb.like(root.get("title"), keyword + "%");

    }

    public static Specification<Retrospective> withBookmark(Boolean isBookmarked, Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (isBookmarked) {
                return criteriaBuilder.equal(root.join("bookmarks").get("user").get("id"), userId);
            }
            return null;
        };
    }

}
