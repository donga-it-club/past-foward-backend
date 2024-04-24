package aws.retrospective.specification;

import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RetrospectiveSpecification {

    public static Specification<Retrospective> withUserId(Long userId) {
        return (root, query, cb) -> {
            Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);

            Join<Retrospective, Team> teamJoin = root.join("team", JoinType.LEFT);
            Join<Team, UserTeam> userTeamJoin = teamJoin.join("users", JoinType.LEFT);
            Join<UserTeam, User> userJoin = userTeamJoin.join("user", JoinType.LEFT);
            Predicate teamPredicate = cb.equal(userJoin.get("id"), userId);
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
