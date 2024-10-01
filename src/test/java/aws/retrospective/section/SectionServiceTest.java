package aws.retrospective.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import aws.retrospective.dto.CreateSectionRequest;
import aws.retrospective.dto.CreateSectionResponse;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.repository.UserTeamRepository;
import aws.retrospective.service.SectionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserTeamRepository userTeamRepository;
    @Autowired
    private RetrospectiveRepository retrospectiveRepository;
    @Autowired
    private RetrospectiveTemplateRepository retrospectiveTemplateRepository;
    @Autowired
    private TemplateSectionRepository templateSectionRepository;
    @Autowired
    private SectionService sectionService;

    @Test
    @DisplayName("팀 회고보드에 회고카드를 작성할 수 있다.")
    void createSection() {
        //given
        User user = createUser();
        User savedUser = userRepository.save(user);
        Team team = createTeam();
        Team savedTeam = teamRepository.save(team);
        UserTeam userTeam = createUserTeam(savedUser, savedTeam);
        userTeamRepository.save(userTeam);

        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        RetrospectiveTemplate savedRetrospectiveTemplate = retrospectiveTemplateRepository.save(
            retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", savedTeam, savedUser,
            savedRetrospectiveTemplate);
        Retrospective savedRetrospective = retrospectiveRepository.save(retrospective);

        TemplateSection templateSection = createTemplateSection("Keep", retrospectiveTemplate);
        TemplateSection savedTemplateSection = templateSectionRepository.save(templateSection);

        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(savedRetrospective.getId())
            .templateSectionId(savedTemplateSection.getId())
            .sectionContent("section")
            .build();

        //when
        CreateSectionResponse response = sectionService.createSection(user, request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getRetrospectiveId()).isEqualTo(savedRetrospective.getId());
        assertThat(response.getSectionContent()).isEqualTo("section");
    }

    @Test
    @DisplayName("회고보드가 조회되지 않으면 회고카드를 생성할 수 없다.")
    void failed_createSection_when_notFound_Retrospective() {
        //given
        User user = createUser();
        User savedUser = userRepository.save(user);
        Team team = createTeam();
        Team savedTeam = teamRepository.save(team);
        UserTeam userTeam = createUserTeam(savedUser, savedTeam);
        userTeamRepository.save(userTeam);

        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        RetrospectiveTemplate savedRetrospectiveTemplate = retrospectiveTemplateRepository.save(
            retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", savedTeam, savedUser,
            savedRetrospectiveTemplate);
        Retrospective savedRetrospective = retrospectiveRepository.save(retrospective);

        TemplateSection templateSection = createTemplateSection("Keep", retrospectiveTemplate);
        TemplateSection savedTemplateSection = templateSectionRepository.save(templateSection);

        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(savedRetrospective.getId() + 1)
            .templateSectionId(savedTemplateSection.getId())
            .sectionContent("section")
            .build();

        //when // then
        assertThatThrownBy(() -> sectionService.createSection(user, request))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("회고카드가 조회되지 않습니다.");
    }

    @Test
    @DisplayName("회고카드 템플릿이 조회되지 않으면 회고카드를 생성할 수 없다.")
    void failed_createSection_when_notFound_TemplateSection() {
        //given
        User user = createUser();
        User savedUser = userRepository.save(user);
        Team team = createTeam();
        Team savedTeam = teamRepository.save(team);
        UserTeam userTeam = createUserTeam(savedUser, savedTeam);
        userTeamRepository.save(userTeam);

        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        RetrospectiveTemplate savedRetrospectiveTemplate = retrospectiveTemplateRepository.save(
            retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", savedTeam, savedUser,
            savedRetrospectiveTemplate);
        Retrospective savedRetrospective = retrospectiveRepository.save(retrospective);

        TemplateSection templateSection = createTemplateSection("Keep", retrospectiveTemplate);
        TemplateSection savedTemplateSection = templateSectionRepository.save(templateSection);

        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(savedRetrospective.getId())
            .templateSectionId(savedTemplateSection.getId() + 1)
            .sectionContent("section")
            .build();

        //when // then
        assertThatThrownBy(() -> sectionService.createSection(user, request))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("회고카드를 작성하기 위한 템플릿이 조회되지 않습니다.");
    }

    private static TemplateSection createTemplateSection(String sectionName,
        RetrospectiveTemplate retrospectiveTemplate) {
        return TemplateSection.builder()
            .sectionName(sectionName)
            .template(retrospectiveTemplate)
            .sequence(0)
            .build();
    }

    private static Retrospective createRetrospective(String title, Team team, User user,
        RetrospectiveTemplate retrospectiveTemplate) {
        return Retrospective.builder()
            .title(title)
            .team(team)
            .user(user)
            .template(retrospectiveTemplate)
            .build();
    }

    private static RetrospectiveTemplate createRetrospectiveTemplate(String templateName) {
        return RetrospectiveTemplate.builder()
            .name(templateName)
            .build();
    }

    private static User createUser() {
        return User.builder()
            .build();
    }

    private static Team createTeam() {
        return Team.builder()
            .build();
    }

    private static UserTeam createUserTeam(User user, Team team) {
        return UserTeam.builder()
            .user(user)
            .team(team)
            .build();
    }

}
