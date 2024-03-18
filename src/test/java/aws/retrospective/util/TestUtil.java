package aws.retrospective.util;

import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;

public class TestUtil {

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

}
