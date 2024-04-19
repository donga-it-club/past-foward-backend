package aws.retrospective.repository;

import static aws.retrospective.entity.QTeam.team;
import static aws.retrospective.entity.QUser.user;
import static aws.retrospective.entity.QUserTeam.userTeam;

import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.dto.QGetTeamUsersResponseDto;
import aws.retrospective.entity.UserTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTeamRepositoryCustomImpl implements UserTeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final UserTeamRepositoryCustom userTeamRepositoryCustom;

    @Override
    public List<GetTeamUsersResponseDto> findTeamMembers(Long teamId) {
        return queryFactory.select(
                        new QGetTeamUsersResponseDto(userTeam.user.id, userTeam.user.username, userTeam.user.thumbnail,
                                userTeam.user.email, userTeam.joinedAt))
                .from(userTeam)
                .join(userTeam.team, team)
                .join(userTeam.user, user)
                .where(userTeam.team.id.eq(teamId))
                .fetch();
    }

    @Override
    public Optional<UserTeam> findByUserIdAndTeamId(Long userId, Long teamId) {
        return userTeamRepositoryCustom.findByUserIdAndTeamId(userId, teamId);
    }


}