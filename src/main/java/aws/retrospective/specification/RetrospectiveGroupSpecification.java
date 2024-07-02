package aws.retrospective.specification;

import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.RetrospectiveGroup;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RetrospectiveGroupSpecification {

    public static Specification<RetrospectiveGroup> withUserId(Long userId) {
        return (root, query, cb) -> {
            Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);

            return cb.or();
        };
    }
    public static Specification<RetrospectiveGroup> withKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return (root, query, cb) -> cb.like(root.get("title"), keyword + "%");

    }
    public static Specification<RetrospectiveGroup> withBookmark(Boolean isBookmarked, Long userId) {
        if (isBookmarked == null || !isBookmarked) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.join("bookmarks").get("user").get("id"), userId);
    }

    public static Specification<RetrospectiveGroup> withStatus(ProjectStatus status) {
        if (status == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
