package aws.retrospective.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import aws.retrospective.dto.CreateSectionRequest;
import aws.retrospective.dto.CreateSectionResponse;
import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import aws.retrospective.exception.retrospective.TemplateMisMatchException;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.repository.UserTeamRepository;
import aws.retrospective.service.SectionService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
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
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("팀 회고보드에 회고카드를 작성할 수 있다.")
    void createSectionForTeamBoard() {
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
    @DisplayName("개인 회고보드에 회고카드를 작성할 수 있다.")
    void createSectionForPersonalBoard() {
        //given
        User user = createUser();
        User savedUser = userRepository.save(user);

        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        RetrospectiveTemplate savedRetrospectiveTemplate = retrospectiveTemplateRepository.save(
            retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, savedUser,
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

    @Test
    @DisplayName("회고보드 템플릿과 회고카드 템플릿이 다르면 회고카드를 생성할 수 없다.")
    void failed_createSection_when_misMatch_template() {
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
        templateSectionRepository.save(templateSection);

        RetrospectiveTemplate retrospectiveTemplate2 = createRetrospectiveTemplate("KWAT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate2);
        TemplateSection templateSection2 = createTemplateSection("KUDOS", retrospectiveTemplate2);
        TemplateSection savedTemplateSection2 = templateSectionRepository.save(templateSection2);

        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(savedRetrospective.getId())
            .templateSectionId(savedTemplateSection2.getId())
            .sectionContent("section")
            .build();

        //when // then
        assertThatThrownBy(() -> sectionService.createSection(user, request))
            .isInstanceOf(TemplateMisMatchException.class);
    }

    @Test
    @DisplayName("개인 회고보드에 작성된 모든 회고카드를 조회할 수 있다.")
    void getSections() {
        //given
        User user = createUser();
        userRepository.save(user);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, user,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);

        TemplateSection keepTemplateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(keepTemplateSection);
        Section section1 = Section.create("content1", retrospective, user, keepTemplateSection);
        Section section2 = Section.create("content2", retrospective, user, keepTemplateSection);
        sectionRepository.save(section1);
        sectionRepository.save(section2);

        TemplateSection problemTemplateSection = createTemplateSection("Problem",
            retrospectiveTemplate);
        templateSectionRepository.save(problemTemplateSection);
        Section section3 = Section.create("content3", retrospective, user, problemTemplateSection);
        sectionRepository.save(section3);

        GetSectionsRequestDto request = GetSectionsRequestDto.builder()
            .retrospectiveId(retrospective.getId())
            .teamId(null)
            .build();

        //when
        List<GetSectionsResponseDto> sections = sectionService.getSection(request);

        //then
        assertThat(sections).hasSize(3);
        assertThat(sections)
            .extracting("sectionId", "userId", "username", "content", "likeCnt", "sectionName",
                 "thumbnail", "comments")
            .containsExactlyInAnyOrder(
                tuple(section1.getId(), user.getId(), user.getUsername(), section1.getContent(),
                    section1.getLikeCnt(),
                    keepTemplateSection.getSectionName(),
                    user.getThumbnail(), Collections.emptyList()),
                tuple(section2.getId(), user.getId(), user.getUsername(), section2.getContent(),
                    section2.getLikeCnt(), keepTemplateSection.getSectionName(),

                    user.getThumbnail(), Collections.emptyList()),
                tuple(section3.getId(), user.getId(), user.getUsername(), section3.getContent(),
                    section3.getLikeCnt(), problemTemplateSection.getSectionName(),

                    user.getThumbnail(), Collections.emptyList())
            );
    }

    @Test
    @DisplayName("팀 회고보드에 작성된 모든 회고카드를 조회할 수 있다.")
    void getSections2() {
        //given
        User user = createUser();
        userRepository.save(user);
        User user2 = createUser();
        userRepository.save(user2);
        RetrospectiveTemplate retrospectiveTemplate = createRetrospectiveTemplate("KPT");
        retrospectiveTemplateRepository.save(retrospectiveTemplate);
        Retrospective retrospective = createRetrospective("title", null, user,
            retrospectiveTemplate);
        retrospectiveRepository.save(retrospective);

        TemplateSection keepTemplateSection = createTemplateSection("Keep", retrospectiveTemplate);
        templateSectionRepository.save(keepTemplateSection);
        Section section1 = Section.create("content1", retrospective, user, keepTemplateSection);
        sectionRepository.save(section1);
        Section section2 = Section.create("content2", retrospective, user2, keepTemplateSection);
        sectionRepository.save(section2);

        TemplateSection problemTemplateSection = createTemplateSection("Problem",
            retrospectiveTemplate);
        templateSectionRepository.save(problemTemplateSection);
        Section section3 = Section.create("content3", retrospective, user, problemTemplateSection);
        sectionRepository.save(section3);

        Comment comment1 = createComment(user, section1, "content1");
        commentRepository.save(comment1);
        Comment comment2 = createComment(user2, section2, "content2");
        commentRepository.save(comment2);

        GetSectionsRequestDto request = GetSectionsRequestDto.builder()
            .retrospectiveId(retrospective.getId())
            .teamId(null)
            .build();

        //when
        List<GetSectionsResponseDto> sections = sectionService.getSection(request);

        //then
        assertThat(sections).hasSize(3);
        assertThat(sections)
            .extracting("sectionId", "userId", "username", "content", "likeCnt", "sectionName", "thumbnail")
            .containsExactlyInAnyOrder(
                tuple(section1.getId(), user.getId(), user.getUsername(), section1.getContent(),
                    section1.getLikeCnt(), keepTemplateSection.getSectionName(), user.getThumbnail()),
                tuple(section2.getId(), user2.getId(), user2.getUsername(), section2.getContent(),
                    section2.getLikeCnt(), keepTemplateSection.getSectionName(), user2.getThumbnail()),
                tuple(section3.getId(), user.getId(), user.getUsername(), section3.getContent(),
                    section3.getLikeCnt(), problemTemplateSection.getSectionName(), user.getThumbnail())
            );

        assertThat(sections.get(0).getComments()).isEmpty();

        assertThat(sections.get(1).getComments()).hasSize(1);
        assertThat(sections.get(1).getComments().get(0).getCommentId()).isEqualTo(comment2.getId());
        assertThat(sections.get(1).getComments().get(0).getUserId()).isEqualTo(user2.getId());
        assertThat(sections.get(1).getComments().get(0).getContent()).isEqualTo(comment2.getContent());

        assertThat(sections.get(2).getComments()).hasSize(1);
        assertThat(sections.get(2).getComments().get(0).getCommentId()).isEqualTo(comment1.getId());
        assertThat(sections.get(2).getComments().get(0).getUserId()).isEqualTo(user.getId());
        assertThat(sections.get(2).getComments().get(0).getContent()).isEqualTo(comment1.getContent());
    }

    private static Comment createComment(User user, Section section, String content) {
        return Comment.builder()
            .user(user)
            .section(section)
            .content(content)
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

    private static UserTeam createUserTeam(User user, Team team) {
        return UserTeam.builder()
            .user(user)
            .team(team)
            .build();
    }

}
