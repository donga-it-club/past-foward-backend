package aws.retrospective.service;

import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import aws.retrospective.entity.UserTeamRole;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserTeamRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final RetrospectiveRepository retrospectiveRepository;
    private final UserTeamRepository userTeamRepository;


    @Transactional(readOnly = true)
    public List<GetTeamUsersResponseDto> getTeamMembers(Long teamId,
        Long retrospectiveId) {
        Team findTeam = getTeam(teamId);
        Retrospective findRetrospective = getRetrospective(retrospectiveId);

        // 다른 팀의 회고보드에 대한 조회는 불가능하다.
        if (findRetrospective.getTeam().getId() != findTeam.getId()) {
            throw new ForbiddenAccessException("해당 팀의 회고보드만 조회할 수 있습니다.");
        }

        return userTeamRepository.findTeamMembers(teamId);
    }

    @Transactional
    public void removeTeamMember(User currentUser, Long teamId, Long userId) {
        Team team = getTeam(teamId);
        UserTeam userTeam = userTeamRepository.findByTeamIdAndRole(teamId, UserTeamRole.LEADER)
            .orElseThrow(
                () -> new IllegalArgumentException("No leader found for team id: " + teamId));

        if (!userTeam.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenAccessException("팀장만 팀원을 삭제할 수 있습니다.");
        }

        if (userTeam.getUser().getId().equals(userId)) {
            throw new ForbiddenAccessException("팀장은 팀을 나갈 수 없습니다.");
        }

        userTeamRepository.deleteByTeamIdAndUserId(team.getId(), userId);
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new NoSuchElementException("Not Found Team id : " + teamId));
    }

    private Retrospective getRetrospective(Long retrospectiveId) {
        return retrospectiveRepository.findById(retrospectiveId).orElseThrow(
            () -> new NoSuchElementException("Not Found Retrospective id : " + retrospectiveId));
    }
}
