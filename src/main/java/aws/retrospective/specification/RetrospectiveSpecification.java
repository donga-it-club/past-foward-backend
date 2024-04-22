package aws.retrospective.specification;

import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RetrospectiveSpecification {

    public static Specification<Retrospective> withUserId(Long userId) {
        return (root, query, cb) -> {
            Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
            Predicate teamPredicate = cb.equal(
                root.join("team", JoinType.LEFT).join("users", JoinType.LEFT)
                    .get("id"), userId);
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
        if (isBookmarked == null || !isBookmarked) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.join("bookmarks").get("user").get("id"), userId);
    }

    public static Specification<Retrospective> withStatus(ProjectStatus status) {
        if (status == null) {
            return null;
        }
        
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

}
