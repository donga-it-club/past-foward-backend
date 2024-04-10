package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.GetTeamUsersRequestDto;
import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserTeamRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;
    @Mock
    RetrospectiveRepository retrospectiveRepository;
    @Mock
    UserTeamRepository userTeamRepository;
    @InjectMocks
    TeamService teamService;

    @Test
    @DisplayName("Action Items를 눌렀을 때 팀에 속한 모든 회원을 조회할 수 있다.")
    void getTeamMembers() {
        //given
        Long teamId = 1L;
        Team createdTeam = createTeam();
        ReflectionTestUtils.setField(createdTeam, "id", teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(createdTeam));

        Long userId = 1L;
        User createdUser = createUser();
        ReflectionTestUtils.setField(createdUser, "id", userId);

        Long userTeamId = 1L;
        UserTeam createdUserTeam = createUserTeam(createdUser, createdTeam);
        ReflectionTestUtils.setField(createdUserTeam, "id", userTeamId);

        GetTeamUsersResponseDto response = new GetTeamUsersResponseDto(userId, "test", "test");
        when(userTeamRepository.findTeamMembers(teamId)).thenReturn(List.of(response));

        Long retrospectiveId = 1L;
        Retrospective createdRetrospective = createRetrospective(createTemplate(), createdUser,
            createdTeam);
        ReflectionTestUtils.setField(createdRetrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(
            Optional.of(createdRetrospective));

        //when
        GetTeamUsersRequestDto request = new GetTeamUsersRequestDto();
        ReflectionTestUtils.setField(request, "retrospectiveId", retrospectiveId);
        List<GetTeamUsersResponseDto> result = teamService.getTeamMembers(teamId,
            request);

        //then
        assertThat(result.size()).isEqualTo(1);
        GetTeamUsersResponseDto searchUser = result.get(0);
        assertThat(searchUser.getUserId()).isEqualTo(userId);
        assertThat(searchUser.getUsername()).isEqualTo("test");
        assertThat(searchUser.getProfileImage()).isEqualTo("test");
    }

    private static RetrospectiveTemplate createTemplate() {
        return RetrospectiveTemplate.builder()
            .name("KPT")
            .build();
    }

    private static Team createTeam() {
        return Team.builder()
            .name("name")
            .build();
    }

    private static User createUser() {
        return User.builder()
            .username("test")
            .password("test")
            .phone("010-1234-1234")
            .email("test@naver.com")
            .build();
    }

    private static UserTeam createUserTeam(User createdUser, Team createdTeam) {
        return UserTeam.builder()
            .user(createdUser)
            .team(createdTeam)
            .build();
    }

    private static Retrospective createRetrospective(RetrospectiveTemplate retrospectiveTemplate,
        User user,
        Team team) {
        return Retrospective.builder()
            .template(retrospectiveTemplate)
            .status(ProjectStatus.IN_PROGRESS)
            .title("test")
            .team(team)
            .user(user)
            .build();
    }
}