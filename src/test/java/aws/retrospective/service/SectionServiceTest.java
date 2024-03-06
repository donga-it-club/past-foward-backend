package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
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
class SectionServiceTest {

    @Mock
    SectionRepository sectionRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RetrospectiveRepository retrospectiveRepository;
    @Mock
    TemplateSectionRepository templateSectionRepository;
    @InjectMocks
    SectionService sectionService;

    @Test
    @DisplayName("섹션 등록 API")
    void createSectionTest() {
        //given
        Long userId = 1L;
        User user = createUser();
        ReflectionTestUtils.setField(user, "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Team team = createTeam();
        RetrospectiveTemplate retrospectiveTemplate = createTemplate();

        Long retrospectiveId = 1L;
        Retrospective retrospective = createRetrospective(retrospectiveTemplate, user, team);
        ReflectionTestUtils.setField(retrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(
            Optional.of(retrospective));

        CreateSectionDto request = new CreateSectionDto();
        request.setSectionContent("test");
        request.setSectionName("test");
        request.setRetrospectiveId(retrospectiveId);
        request.setUserId(userId);

        //when
        CreateSectionResponseDto response = sectionService.createSection(request);

        //then
        assertThat(response.getSectionName()).isEqualTo(request.getSectionName());
        assertThat(response.getSectionContent()).isEqualTo(request.getSectionContent());
        assertThat(response.getRetrospectiveId()).isEqualTo(request.getRetrospectiveId());
        assertThat(response.getUserId()).isEqualTo(request.getUserId());


    }

    private static Retrospective createRetrospective(RetrospectiveTemplate retrospectiveTemplate,
        User user,
        Team team) {
        return Retrospective.builder()
            .title("test")
            .template(retrospectiveTemplate)
            .status(ProjectStatus.IN_PROGRESS)
            .user(user)
            .team(team)
            .build();
    }

    private static RetrospectiveTemplate createTemplate() {
        return RetrospectiveTemplate.builder()
            .name("test")
            .build();
    }

    private static Team createTeam() {
        return Team.builder()
            .name("test")
            .build();
    }

    private static User createUser() {
        return User.builder()
            .email("test")
            .phone("test")
            .password("test")
            .username("test")
            .build();
    }

}