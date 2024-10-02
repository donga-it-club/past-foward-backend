package aws.retrospective.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import aws.retrospective.entity.Comment;
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
class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RetrospectiveRepository retrospectiveRepository;
    @Autowired
    private RetrospectiveTemplateRepository retrospectiveTemplateRepository;
    @Autowired
    private TemplateSectionRepository templateSectionRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("개인 회고보드에 속한 회고 카드에 달린 댓글을 조회할 수 있다.")
    void findAllBySectionIdIn() {
        //given
        User user = createUser();
        userRepository.save(user);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, user,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);
        TemplateSection templateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(templateSection);

        Section section1 = createSection("content", retrospective, user, templateSection);
        sectionRepository.save(section1);
        Section section2 = createSection("content", retrospective, user, templateSection);
        sectionRepository.save(section2);

        Comment comment1 = createComment("content1", section1, user);
        commentRepository.save(comment1);
        Comment comment2 = createComment("content2", section2, user);
        commentRepository.save(comment2);

        //when
        List<Comment> comments = commentRepository.findAllBySectionIdIn(
            List.of(section1.getId(), section2.getId()));

        //then
        assertThat(comments).hasSize(2);
        assertThat(comments)
            .extracting("id", "content", "user", "section")
            .containsExactlyInAnyOrder(
                tuple(comment1.getId(), comment1.getContent(), user, section1),
                tuple(comment2.getId(), comment2.getContent(), user, section2)
            );
    }

    @Test
    @DisplayName("팀 회고보드에 속한 회고 카드에 달린 댓글을 조회할 수 있다.")
    void findAllBySectionIdIn2() {
        //given
        User user1 = createUser();
        userRepository.save(user1);
        User user2 = createUser();
        userRepository.save(user2);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Team team = createTeam();
        teamRepository.save(team);
        Retrospective retrospective = createRetrospective("title", team, user1,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);
        TemplateSection templateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(templateSection);

        Section section1 = createSection("content", retrospective, user1, templateSection);
        sectionRepository.save(section1);
        Section section2 = createSection("content", retrospective, user1, templateSection);
        sectionRepository.save(section2);

        Comment comment1 = createComment("content1", section1, user1);
        commentRepository.save(comment1);
        Comment comment2 = createComment("content2", section2, user2);
        commentRepository.save(comment2);

        //when
        List<Comment> comments = commentRepository.findAllBySectionIdIn(
            List.of(section1.getId(), section2.getId()));

        //then
        assertThat(comments).hasSize(2);
        assertThat(comments)
            .extracting("id", "content", "user", "section")
            .containsExactlyInAnyOrder(
                tuple(comment1.getId(), comment1.getContent(), user1, section1),
                tuple(comment2.getId(), comment2.getContent(), user2, section2)
            );
    }

    private static Comment createComment(String content, Section section, User user) {
        return Comment.builder()
            .section(section)
            .content(content)
            .user(user)
            .build();
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

    private static Section createSection(String content, Retrospective retrospective, User user,
        TemplateSection keepTemplateSection) {
        return Section.create(content, retrospective, user, keepTemplateSection);
    }

}