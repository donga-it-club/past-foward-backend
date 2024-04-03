package aws.retrospective.util;

import aws.retrospective.entity.Bookmark;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestUtil {

    private TestUtil() {
    }

    public static Section createSection(User loginedUser) {
        return Section.builder()
            .user(loginedUser)
            .content("test")
            .likeCnt(0)
            .build();
    }

    public static Retrospective createRetrospective(RetrospectiveTemplate retrospectiveTemplate,
        User user,
        Team team) {
        return Retrospective.builder()
            .template(retrospectiveTemplate)
            .status(ProjectStatus.IN_PROGRESS)
            .title("test")
            .team(team)
            .user(user)
            .thumbnail(UUID.randomUUID())
            .startDate(LocalDateTime.now())
            .build();
    }

    public static RetrospectiveTemplate createTemplate() {
        return RetrospectiveTemplate.builder()
            .name("KPT")
            .build();
    }

    public static Team createTeam() {
        return Team.builder()
            .name("name")
            .build();
    }

    public static User createUser() {
        return User.builder()
            .username("test")
            .password("test")
            .phone("010-1234-1234")
            .email("test@naver.com")
            .build();
    }

    public static Bookmark createBookmark(User user, Retrospective retrospective) {
        return Bookmark.builder()
            .user(user)
            .retrospective(retrospective)
            .build();
    }

}
