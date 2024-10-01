package aws.retrospective.retrospective;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import aws.retrospective.exception.retrospective.TemplateMisMatchException;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.repository.UserTeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RetrospectiveTest {

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

    @Test
    @DisplayName("회고카드 템플릿과 회고카드 템플릿이 일치하면 예외가 발생하지 않는다.")
    void notThrowException_when_template_match() {
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

        TemplateSection templateSection = createTemplateSection("Kudos",
            savedRetrospectiveTemplate);
        TemplateSection savedTemplateSection = templateSectionRepository.save(templateSection);

        //when // then
        assertDoesNotThrow(
            () -> savedRetrospective.isTemplateSectionIncludedInRetrospectiveTemplate(savedTemplateSection)
        );

    }

    @Test
    @DisplayName("회고카드 템플릿과 회고카드 템플릿이 일치하지 않으면 예외가 발생한다.")
    void shouldThrowException_when_template_mismatch() {
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

        RetrospectiveTemplate retrospectiveTemplate2 = createRetrospectiveTemplate("KWAT");
        RetrospectiveTemplate savedRetrospectiveTemplate2 = retrospectiveTemplateRepository.save(
            retrospectiveTemplate2);
        TemplateSection templateSection = createTemplateSection("Kudos",
            savedRetrospectiveTemplate2);
        TemplateSection savedTemplateSection = templateSectionRepository.save(templateSection);

        //when // then
        assertThatThrownBy(
                () -> savedRetrospective.isTemplateSectionIncludedInRetrospectiveTemplate(savedTemplateSection))
            .isInstanceOf(TemplateMisMatchException.class);
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