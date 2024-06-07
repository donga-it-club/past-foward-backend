package aws.retrospective.repository;

import static aws.retrospective.entity.QBookmark.bookmark;
import static aws.retrospective.entity.QRetrospective.retrospective;
import static aws.retrospective.entity.QTeam.team;
import static aws.retrospective.entity.QUserTeam.userTeam;

import aws.retrospective.dto.GetRetrospectivesDto;
import aws.retrospective.dto.RetrospectivesOrderType;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RetrospectiveCustomRepositoryImpl implements RetrospectiveCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Retrospective> findRetrospectives(User user, GetRetrospectivesDto dto) {
        BooleanExpression predicate = retrospective.deletedDate.isNull()
            .and(retrospective.user.id.eq(user.getId())
                .or(userTeam.user.id.eq(user.getId())));

        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            predicate = predicate.and(retrospective.title.like(dto.getKeyword() + "%"));
        }

        if (dto.getStatus() != null) {
            predicate = predicate.and(retrospective.status.eq(dto.getStatus()));
        }

        if (dto.getIsBookmarked() != null && dto.getIsBookmarked()) {
            predicate = predicate.and(retrospective.id.in(
                JPAExpressions.select(bookmark.retrospective.id)
                    .from(bookmark)
                    .where(bookmark.user.id.eq(user.getId()))
            ));
        }

        return queryFactory.selectFrom(retrospective)
            .distinct()
            .leftJoin(retrospective.team, team)
            .leftJoin(team.userTeams, userTeam)
            .leftJoin(userTeam.user)
            .where(predicate)
            .orderBy(
                dto.getOrder() == RetrospectivesOrderType.OLDEST ? retrospective.createdDate.asc()
                    .nullsLast() : retrospective.createdDate.desc().nullsLast())
            .offset((long) dto.getPage() * dto.getSize())
            .limit(dto.getSize())
            .fetch();
    }

    @Override
    public long countRetrospectives(User user, GetRetrospectivesDto dto) {
        BooleanExpression predicate = retrospective.deletedDate.isNull()
            .and(retrospective.user.id.eq(user.getId())
                .or(userTeam.user.id.eq(user.getId())));

        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            predicate = predicate.and(retrospective.title.like(dto.getKeyword() + "%"));
        }

        if (dto.getStatus() != null) {
            predicate = predicate.and(retrospective.status.eq(dto.getStatus()));
        }

        if (dto.getIsBookmarked() != null && dto.getIsBookmarked()) {
            predicate = predicate.and(retrospective.id.in(
                JPAExpressions.select(bookmark.retrospective.id)
                    .from(bookmark)
                    .where(bookmark.user.id.eq(user.getId()))
            ));
        }

        return queryFactory.selectFrom(retrospective)
            .distinct()
            .leftJoin(retrospective.team, team)
            .leftJoin(team.userTeams, userTeam)
            .leftJoin(userTeam.user)
            .where(predicate)
            .fetch().size();
    }
}
