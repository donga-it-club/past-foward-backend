package aws.retrospective.redis;

import static org.assertj.core.api.Assertions.assertThat;

import aws.retrospective.config.EmbeddedRedisConfig;
import aws.retrospective.dto.GetCommentResponseDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Section.SectionBuilder;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.service.SectionService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("local")
@Transactional
public class RedisNotificationTest {

    @Autowired
    SectionService sectionService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    RetrospectiveRepository retrospectiveRepository;
    @Autowired
    RetrospectiveTemplateRepository retrospectiveTemplateRepository;
    @Autowired
    TemplateSectionRepository templateSectionRepository;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("새로운 댓글이 작성되면 알림을 조회할 수 있다.")
    void commentNotificationTest() {

        User user = getUser();
        userRepository.save(user);
        Team team = getTeam();
        teamRepository.save(team);
        RetrospectiveTemplate kptTemplate = getKptTemplate();
        retrospectiveTemplateRepository.save(kptTemplate);
        Retrospective retrospective = getRetrospective(team, user, kptTemplate);
        retrospectiveRepository.save(retrospective);
        TemplateSection templateSection = getTemplateSection(kptTemplate);
        templateSectionRepository.save(templateSection);
        Section section = getSectionBuilder(templateSection, user, retrospective)
            .content("keep section").likeCnt(0).build();
        sectionRepository.save(section);

        User commentWriter1 = getUser(); // 마지막으로 알림이 조회되는 시점에 댓글을 남긴 사용자1
        userRepository.save(commentWriter1);
        Comment prevComment = getComment(section, commentWriter1); // 사용자1이 작성한 댓글
        commentRepository.save(prevComment);

        // 마지막으로 알림이 조회되는 시점을 저장 -> 사용자1의 댓글이 마지막으로 조회된 시점
        redisTemplate.opsForValue()
            .set(section.getId().toString(), prevComment.getCreatedDate().toString());
        // 사용자1이 작성한 댓글 시간
        LocalDateTime lastCommentTime = sectionService.getLastCommentTime(section.getId());

        User commentWriter2 = getUser(); // 마지막으로 알림이 조회되는 시점 이후에 댓글을 남긴 사용자2
        userRepository.save(commentWriter2);
        Comment newComment = getComment(section, commentWriter2); // 사용자2가 작성한 댓글
        commentRepository.save(newComment);

        // 사용자1의 댓글이 마지막으로 조회된 시점 이후에 작성된 댓글 조회
        List<GetCommentResponseDto> response = sectionService.getNewComments(section.getId(),
            lastCommentTime);

        // 사용자1의 댓글은 조회되지 않고 사용자2의 댓글만 조회되어야 한다.
        assertThat(response.size()).isEqualTo(1);
        GetCommentResponseDto comment = response.get(0);
        assertThat(comment.getCreatedDate()).isEqualTo(newComment.getCreatedDate());
        assertThat(comment.getUsername()).isEqualTo(commentWriter2.getUsername());
        assertThat(comment.getSectionId()).isEqualTo(section.getId());
        assertThat(comment.getRetrospectiveId()).isEqualTo(retrospective.getId());
    }

    private static Comment getComment(Section section, User user) {
        return Comment.builder().section(section).user(user).content("comment1")
            .build();
    }

    private static SectionBuilder getSectionBuilder(TemplateSection templateSection, User user,
        Retrospective retrospective) {
        return Section.builder().templateSection(templateSection).user(user)
            .retrospective(retrospective);
    }

    private static TemplateSection getTemplateSection(RetrospectiveTemplate kptTemplate) {
        return TemplateSection.builder().sectionName("Keep").sequence(1)
            .template(kptTemplate).build();
    }

    private static Retrospective getRetrospective(Team team, User user,
        RetrospectiveTemplate kptTemplate) {
        return Retrospective.builder().team(team).user(user).template(kptTemplate)
            .status(ProjectStatus.IN_PROGRESS)
            .title("title").build();
    }

    private static Team getTeam() {
        return Team.builder().name("teamA").build();
    }

    private static RetrospectiveTemplate getKptTemplate() {
        return RetrospectiveTemplate.builder().name("KPT")
            .build();
    }

    private static User getUser() {
        return User.builder().email("test@example.com")
            .phone("010-1234-1234")
            .username("test")
            .build();
    }
}
