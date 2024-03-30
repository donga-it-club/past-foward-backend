package aws.retrospective.repository;

import static aws.retrospective.entity.QTeam.team;
import static aws.retrospective.entity.QUser.user;
import static aws.retrospective.entity.QUserTeam.userTeam;

import aws.retrospective.dto.GetTeamMembersResponseDto;
import aws.retrospective.dto.QGetTeamMembersResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTeamRepositoryCustomImpl implements UserTeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetTeamMembersResponseDto> findTeamMembers(Long teamId) {
        return queryFactory.select(
                new QGetTeamMembersResponseDto(userTeam.user.id, userTeam.user.username))
            .from(userTeam)
            .join(userTeam.team, team)
            .join(userTeam.user, user)
            .where(userTeam.team.id.eq(teamId))
            .fetch();
    }
}
