package aws.retrospective.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.entity.ActionItem;
import aws.retrospective.entity.KudosTarget;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SectionRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RetrospectiveRepository retrospectiveRepository;
    @Autowired
    private RetrospectiveTemplateRepository retrospectiveTemplateRepository;
    @Autowired
    private TemplateSectionRepository templateSectionRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ActionItemRepository actionItemRepository;
    @Autowired
    private KudosTargetRepository kudosTargetRepository;

    @Test
    @DisplayName("회고보드에 작성된 모든 회고카드를 조회할 수 있다.")
    void getSections2() {
        //given
        User user = createUser("username", "thumbnail");
        userRepository.save(user);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, user,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);

        TemplateSection keepTemplateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(keepTemplateSection);
        TemplateSection problemTemplateSection = createTemplateSection("Problem",
            retrospectiveTemplate);
        templateSectionRepository.save(problemTemplateSection);
        Section section1 = createSection("content1", retrospective, user, keepTemplateSection);
        Section section2 = createSection("content2", retrospective, user, problemTemplateSection);
        sectionRepository.save(section1);
        sectionRepository.save(section2);

        //when
        List<GetSectionsResponseDto> sections = sectionRepository.getSections(
            retrospective.getId());

        //then
        assertThat(sections).hasSize(2);
        assertThat(sections)
            .extracting("sectionId", "userId", "username", "content", "likeCnt", "sectionName",
                "thumbnail")
            .containsExactlyInAnyOrder(
                tuple(section1.getId(), user.getId(), user.getUsername(), section1.getContent(),
                    section1.getLikeCnt(), keepTemplateSection.getSectionName(),
                    section1.getCreatedDate(), user.getThumbnail()),
                tuple(section2.getId(), user.getId(), user.getUsername(), section2.getContent(),
                    section2.getLikeCnt(), problemTemplateSection.getSectionName(),
                    section2.getCreatedDate(), user.getThumbnail())
            );
    }

    @Test
    @DisplayName("Kudos로 지정된 사용자 정보를 포함하여 회고카드를 조회한다.")
    void getSectionsWithKudos() {
        //given
        User user = createUser("username", "thumbnail");
        userRepository.save(user);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, user,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);

        TemplateSection keepTemplateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(keepTemplateSection);
        TemplateSection problemTemplateSection = createTemplateSection("Problem",
            retrospectiveTemplate);
        templateSectionRepository.save(problemTemplateSection);
        Section section1 = createSection("content1", retrospective, user, keepTemplateSection);
        Section section2 = createSection("content2", retrospective, user, problemTemplateSection);
        sectionRepository.save(section1);
        sectionRepository.save(section2);

        KudosTarget kudosTarget = KudosTarget.builder()
            .section(section1)
            .user(user)
            .build();
        kudosTargetRepository.save(kudosTarget);

        //when
        List<GetSectionsResponseDto> sections = sectionRepository.getSections(
            retrospective.getId());

        //then
        assertThat(sections).hasSize(2);
        assertThat(sections)
            .extracting("sectionId", "userId", "username", "content", "likeCnt", "sectionName",
                "thumbnail")
            .containsExactlyInAnyOrder(
                tuple(section1.getId(), user.getId(), user.getUsername(), section1.getContent(),
                    section1.getLikeCnt(), keepTemplateSection.getSectionName(),
                    section1.getCreatedDate(), user.getThumbnail()),
                tuple(section2.getId(), user.getId(), user.getUsername(), section2.getContent(),
                    section2.getLikeCnt(), problemTemplateSection.getSectionName(),
                    section2.getCreatedDate(), user.getThumbnail())
            );
        assertThat(sections.get(0).getKudosTarget()).isNull();
        assertThat(sections.get(1).getKudosTarget()).isNotNull();
    }

    @Test
    @DisplayName("Action Item으로 지정된 사용자 정보를 포함하여 회고카드를 조회한다.")
    void getSectionsWithActionItem() {
        //given
        User user = createUser("username", "thumbnail");
        userRepository.save(user);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, user,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);

        TemplateSection keepTemplateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(keepTemplateSection);
        TemplateSection problemTemplateSection = createTemplateSection("Problem",
            retrospectiveTemplate);
        templateSectionRepository.save(problemTemplateSection);
        Section section1 = createSection("content1", retrospective, user, keepTemplateSection);
        Section section2 = createSection("content2", retrospective, user, problemTemplateSection);
        sectionRepository.save(section1);
        sectionRepository.save(section2);

        ActionItem actionItem = ActionItem.builder()
            .user(user)
            .section(section1)
            .build();
        actionItemRepository.save(actionItem);

        //when
        List<GetSectionsResponseDto> sections = sectionRepository.getSections(
            retrospective.getId());

        //then
        assertThat(sections).hasSize(2);
        assertThat(sections)
            .extracting("sectionId", "userId", "username", "content", "likeCnt", "sectionName",
                "thumbnail")
            .containsExactlyInAnyOrder(
                tuple(section1.getId(), user.getId(), user.getUsername(), section1.getContent(),
                    section1.getLikeCnt(), keepTemplateSection.getSectionName(),
                    section1.getCreatedDate(), user.getThumbnail()),
                tuple(section2.getId(), user.getId(), user.getUsername(), section2.getContent(),
                    section2.getLikeCnt(), problemTemplateSection.getSectionName(),
                    section2.getCreatedDate(), user.getThumbnail())
            );
        assertThat(sections.get(0).getActionItems()).isNull();
        assertThat(sections.get(1).getActionItems()).isNotNull();
    }

    @Test
    @DisplayName("Action Item과 Kudos Target 둘다 지정된 사용자 정보를 포함하여 회고카드를 조회한다.")
    void getSectionsWithKudosAndActionItem() {
        //given
        User user = createUser("username", "thumbnail");
        userRepository.save(user);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, user,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);

        TemplateSection keepTemplateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(keepTemplateSection);
        TemplateSection problemTemplateSection = createTemplateSection("Problem",
            retrospectiveTemplate);
        templateSectionRepository.save(problemTemplateSection);
        Section section1 = createSection("content1", retrospective, user, keepTemplateSection);
        Section section2 = createSection("content2", retrospective, user, problemTemplateSection);
        sectionRepository.save(section1);
        sectionRepository.save(section2);

        ActionItem actionItem = ActionItem.builder()
            .user(user)
            .section(section1)
            .build();
        actionItemRepository.save(actionItem);
        actionItemRepository.save(actionItem);
        KudosTarget kudosTarget = KudosTarget.builder()
            .user(user)
            .section(section1)
            .build();
        kudosTargetRepository.save(kudosTarget);

        //when
        List<GetSectionsResponseDto> sections = sectionRepository.getSections(
            retrospective.getId());

        //then
        assertThat(sections).hasSize(2);
        assertThat(sections)
            .extracting("sectionId", "userId", "username", "content", "likeCnt", "sectionName",
                "thumbnail")
            .containsExactlyInAnyOrder(
                tuple(section1.getId(), user.getId(), user.getUsername(), section1.getContent(),
                    section1.getLikeCnt(), keepTemplateSection.getSectionName(),
                    section1.getCreatedDate(), user.getThumbnail()),
                tuple(section2.getId(), user.getId(), user.getUsername(), section2.getContent(),
                    section2.getLikeCnt(), problemTemplateSection.getSectionName(),
                    section2.getCreatedDate(), user.getThumbnail())
            );
        assertThat(sections.get(0).getActionItems()).isNull();
        assertThat(sections.get(0).getKudosTarget()).isNull();
        assertThat(sections.get(1).getActionItems()).isNotNull();
        assertThat(sections.get(1).getKudosTarget()).isNotNull();
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

    private static User createUser(String username, String thumbnail) {
        User user = User.builder()
            .username(username)
            .build();
        user.updateUserInfo(thumbnail, username);
        return user;
    }

    private static Section createSection(String content, Retrospective retrospective, User user,
        TemplateSection keepTemplateSection) {
        return Section.create(content, retrospective, user, keepTemplateSection);
    }

}