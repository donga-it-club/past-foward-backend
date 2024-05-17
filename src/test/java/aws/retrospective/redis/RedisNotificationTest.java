package aws.retrospective.redis;

import static org.assertj.core.api.Assertions.assertThat;

import aws.retrospective.dto.SectionNotificationDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Section.SectionBuilder;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.LikesRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.service.NotificationService;
import aws.retrospective.service.SectionService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
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
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    NotificationService notificationService;

    record Result(User user, Retrospective retrospective, Section section) {
    }

    private Result init() {
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

        return new Result(user, retrospective, section);
    }

    @Test
    @DisplayName("회고 카드에 처음으로 작성된 댓글을 알림으로 받는다.")
    void receiveNotificationForFirstComment() {
        //given
        Result init = init();

        Comment comment = getComment(init.section(), init.user());
        commentRepository.save(comment);

        //when
        List<SectionNotificationDto> response = notificationService.getNewCommentsAndLikes();

        //then
        SectionNotificationDto notification = response.get(0);
        assertThat(notification.getSectionId()).isEqualTo(init.section().getId());
        assertThat(notification.getRetrospectiveId()).isEqualTo(init.retrospective().getId());
        assertThat(notification.getComments().get(0).getUsername()).isEqualTo(
            init.user().getUsername());
        assertThat(notification.getComments().get(0).getCreatedDate()).isEqualTo(
            comment.getCreatedDate());
        assertThat(notification.getLikes()).isEmpty();
    }


    @Test
    @DisplayName("회고 카드에 처음으로 눌린 좋아요를 알림으로 받는다.")
    void receiveNotificationForFirstLike() {
        //given
        Result init = init();

        Likes like = getLikes(init.section, init.user);
        likesRepository.save(like);

        //when
        List<SectionNotificationDto> response = notificationService.getNewCommentsAndLikes();

        //then
        SectionNotificationDto notification = response.get(0);
        assertThat(notification.getSectionId()).isEqualTo(init.section().getId());
        assertThat(notification.getRetrospectiveId()).isEqualTo(init.retrospective().getId());
        assertThat(notification.getLikes().get(0).getUsername()).isEqualTo(
            init.user().getUsername());
        assertThat(notification.getLikes().get(0).getCreatedDate()).isEqualTo(
            like.getCreatedDate());
        assertThat(notification.getComments()).isEmpty();
    }

    @Test
    @DisplayName("알림을 받은 후 새로운 댓글이 작성돠면 추가 알림을 전송 받는다")
    void receiveNotificationForNewCommentAfterNotification() {
        //given
        Result init = init();

        // 이미 알림을 받은 댓글
        Comment comment = getComment(init.section(), init.user());
        commentRepository.save(comment);

        redisTemplate.opsForHash().put("sectionId_" + init.section().getId(), "comment",
            comment.getCreatedDate().toString());

        // 알림 이후에 작성된 댓글
        Comment newComment = getComment(init.section(), init.user());
        commentRepository.save(newComment);

        //when
        List<SectionNotificationDto> response = notificationService.getNewCommentsAndLikes();

        //then
        SectionNotificationDto notification = response.get(0);
        assertThat(notification.getSectionId()).isEqualTo(init.section().getId());
        assertThat(notification.getRetrospectiveId()).isEqualTo(init.retrospective().getId());
        assertThat(notification.getComments().get(0).getUsername()).isEqualTo(
            init.user().getUsername());
        assertThat(notification.getComments().get(0).getCreatedDate()).isEqualTo(
            newComment.getCreatedDate());
        assertThat(notification.getLikes()).isEmpty();
    }

    @Test
    @DisplayName("알림을 받은 후 새로운 좋아요가 눌리면 추가 알림을 전송 받는다")
    void receiveNotificationForNewLikeAfterNotification() {
        //given
        Result init = init();

        // 이미 알림을 받은 댓글
        Likes oldLike = getLikes(init.section(), init.user());
        likesRepository.save(oldLike);

        redisTemplate.opsForHash().put("sectionId_" + init.section().getId(), "comment",
            oldLike.getCreatedDate().toString());

        // 알림 이후에 작성된 댓글
        Likes newLike = getLikes(init.section(), init.user());
        likesRepository.save(newLike);

        //when
        List<SectionNotificationDto> response = notificationService.getNewCommentsAndLikes();

        //then
        SectionNotificationDto notification = response.get(0);
        assertThat(notification.getSectionId()).isEqualTo(init.section().getId());
        assertThat(notification.getRetrospectiveId()).isEqualTo(init.retrospective().getId());
        assertThat(notification.getLikes().get(0).getUsername()).isEqualTo(
            init.user().getUsername());
        assertThat(notification.getLikes().get(0).getCreatedDate()).isEqualTo(
            newLike.getCreatedDate());
        assertThat(notification.getComments()).isEmpty();
    }

    private static Likes getLikes(Section section, User user) {
        return Likes.builder().section(section).user(user).build();
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
