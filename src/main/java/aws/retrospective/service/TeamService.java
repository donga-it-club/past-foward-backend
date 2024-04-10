package aws.retrospective.service;

import aws.retrospective.dto.GetTeamUsersRequestDto;
import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Team;
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
    public List<GetTeamUsersResponseDto> getTeamMembers(Long teamId, GetTeamUsersRequestDto request) {
        Team findTeam = getTeam(teamId);
        Retrospective findRetrospective = getRetrospective(request.getRetrospectiveId());

        // 다른 팀의 회고보드에 대한 조회는 불가능하다.
        if(findRetrospective.getTeam().getId() != findTeam.getId()) {
            throw new ForbiddenAccessException("해당 팀의 회고보드만 조회할 수 있습니다.");
        }

        return userTeamRepository.findTeamMembers(teamId);
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(
                () -> new NoSuchElementException("Not Found Team id : " + teamId));
    }

    private Retrospective getRetrospective(Long retrospectiveId) {
        return retrospectiveRepository.findById(retrospectiveId)
            .orElseThrow(() -> new NoSuchElementException(
                "Not Found Retrospective id : " + retrospectiveId));
    }
}
