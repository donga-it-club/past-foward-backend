package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class RetrospectiveServiceTest {

    @Mock
    private RetrospectiveRepository retrospectiveRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RetrospectiveTemplateRepository templateRepository;

    @InjectMocks
    private RetrospectiveService retrospectiveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRetrospective_ReturnsResponseDto_WhenCalledWithValidDto() {
        // given
        User user = new User("user1", "test", "test", "test");
        ReflectionTestUtils.setField(user, "id", 1L);
        BDDMockito.given(userRepository.findById(1L)).willReturn(Optional.of(user));

        Team team = new Team("Team Name");
        ReflectionTestUtils.setField(team, "id", 1L);
        BDDMockito.given(teamRepository.findById(1L)).willReturn(Optional.of(team));

        RetrospectiveTemplate template = new RetrospectiveTemplate("Template Name");
        ReflectionTestUtils.setField(template, "id", 1L);
        BDDMockito.given(templateRepository.findById(1L)).willReturn(Optional.of(template));

        Retrospective retrospective = new Retrospective("New Retro",
            ProjectStatus.IN_PROGRESS,
            team, user, template);

        ReflectionTestUtils.setField(retrospective, "id", 1L);
        BDDMockito.given(retrospectiveRepository.save(any(Retrospective.class)))
            .willReturn(retrospective);

        CreateRetrospectiveDto dto = new CreateRetrospectiveDto();
        dto.setTitle("New Retro");
        dto.setTeamId(team.getId());
        dto.setUserId(user.getId());
        dto.setTemplateId(template.getId());

        // when
        CreateRetrospectiveResponseDto response = retrospectiveService.createRetrospective(dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getTeamId()).isEqualTo(team.getId());
        assertThat(response.getTemplateId()).isEqualTo(template.getId());
    }

}
