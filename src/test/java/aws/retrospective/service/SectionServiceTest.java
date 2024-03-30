package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.DeleteSectionRequestDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.FindSectionCountRequestDto;
import aws.retrospective.dto.FindSectionCountResponseDto;
import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.dto.GetTeamMembersRequestDto;
import aws.retrospective.dto.GetTeamMembersResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesRequestDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.entity.UserTeam;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.repository.LikesRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.repository.UserTeamRepository;
import java.util.List;
import java.util.NoSuchElementException;
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
    @Mock
    LikesRepository likesRepository;
    @Mock
    TeamRepository teamRepository;
    @Mock
    UserTeamRepository userTeamRepository;
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
        RetrospectiveTemplate kptTemplate = createTemplate();

        Long retrospectiveId = 1L;
        Retrospective retrospective = createRetrospective(kptTemplate, user, team);
        ReflectionTestUtils.setField(retrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(
            Optional.of(retrospective));

        Long templateSectionId = 1L;
        TemplateSection templateSection = createTemplateSection(kptTemplate);
        ReflectionTestUtils.setField(templateSection, "id", templateSectionId);
        when(templateSectionRepository.findById(templateSectionId)).thenReturn(
            Optional.of(templateSection));

        CreateSectionDto request = new CreateSectionDto();
        ReflectionTestUtils.setField(request, "userId", userId);
        ReflectionTestUtils.setField(request, "retrospectiveId", retrospectiveId);
        ReflectionTestUtils.setField(request, "templateSectionId", templateSectionId);
        ReflectionTestUtils.setField(request, "sectionContent", "test");

        //when
        CreateSectionResponseDto response = sectionService.createSection(request);

        //then
        assertThat(response.getSectionContent()).isEqualTo("test");
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getRetrospectiveId()).isEqualTo(retrospectiveId);
    }

    @Test
    @DisplayName("섹션 삭제 성공 API")
    void deleteSectionSuccessTest() {
        //given
        User user = createUser();
        Team team = createTeam();
        RetrospectiveTemplate kptTemplate = createTemplate();
        TemplateSection templateSection = createTemplateSection(kptTemplate);
        Retrospective retrospective = createRetrospective(kptTemplate, user, team);

        Long sectionId = 1L;
        Section section = createSection(user, templateSection, retrospective);
        ReflectionTestUtils.setField(section, "id", sectionId);
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        DeleteSectionRequestDto request = new DeleteSectionRequestDto();
        ReflectionTestUtils.setField(request, "userId", user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //when
        sectionService.deleteSection(sectionId, request);

        //then
        verify(sectionRepository).delete(section);
    }

    @Test
    @DisplayName("존재하지 않는 섹션을 삭제 시 예외가 발생한다.")
    void deleteSectionFailTest() {

        //given
        Long notExistSectionId = 1L;

        DeleteSectionRequestDto request = new DeleteSectionRequestDto();
        ReflectionTestUtils.setField(request, "userId", 1L);

        //when
        when(sectionRepository.findById(notExistSectionId)).thenThrow(NoSuchElementException.class);
        //then
        assertThrows(NoSuchElementException.class,
            () -> sectionService.deleteSection(notExistSectionId, request));
    }

    @Test
    @DisplayName("사용자가 좋아요를 누른 적이 없다면 좋아요 횟수 1이 증가한다")
    void increaseLikesCnt() {
        //given
        Long userId = 1L;
        User user = createUser();
        ReflectionTestUtils.setField(user, "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Long sectionId = 1L;
        Section section = createSection(user);
        ReflectionTestUtils.setField(section, "id", sectionId);
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        when(likesRepository.findByUserAndSection(user, section)).thenReturn(Optional.empty());

        IncreaseSectionLikesRequestDto request = new IncreaseSectionLikesRequestDto();
        ReflectionTestUtils.setField(request, "userId", userId);

        //when
        IncreaseSectionLikesResponseDto response = sectionService.increaseSectionLikes(
            sectionId, request);

        //then
        assertThat(response.getSectionId()).isEqualTo(sectionId);
        assertThat(response.getLikeCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자는 좋아요를 취소할 수 있다.")
    void decreaseLikesCnt() {
        //given
        Long userId = 1L;
        User user = createUser();
        ReflectionTestUtils.setField(user, "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Long sectionId = 1L;
        Section section = createSection(user);
        ReflectionTestUtils.setField(section, "id", sectionId);
        ReflectionTestUtils.setField(section, "likeCnt", 2);
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        Likes likes = createLikes(section, user);
        when(likesRepository.findByUserAndSection(user, section)).thenReturn(Optional.of(likes));

        IncreaseSectionLikesRequestDto request = new IncreaseSectionLikesRequestDto();
        ReflectionTestUtils.setField(request, "userId", userId);

        //when
        IncreaseSectionLikesResponseDto response = sectionService.increaseSectionLikes(
            sectionId, request);

        //then
        assertThat(response.getSectionId()).isEqualTo(sectionId);
        assertThat(response.getLikeCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("회고보드의 각 섹션에 등록된 게시물 개수를 조회 할 수 있다.")
    void getSectionCountsTest() {
        //given
        Long retrospectiveId = 1L;
        Retrospective retrospective = createRetrospective(createTemplate(), createUser(), createTeam());
        ReflectionTestUtils.setField(retrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(Optional.of(retrospective));

        Long templateSectionId = 1L;
        TemplateSection templateSection = createTemplateSection(createTemplate());
        ReflectionTestUtils.setField(templateSection, "id", templateSectionId);
        when(templateSectionRepository.findById(templateSectionId)).thenReturn(Optional.of(templateSection));

        when(sectionRepository.countByRetrospectiveAndTemplateSection(retrospective, templateSection)).thenReturn(1);

        //when
        FindSectionCountRequestDto request = new FindSectionCountRequestDto();
        ReflectionTestUtils.setField(request, "retrospectiveId", retrospectiveId);
        ReflectionTestUtils.setField(request, "templateSectionId", templateSectionId);
        FindSectionCountResponseDto response = sectionService.getSectionCounts(request);

        //then
        assertThat(response.getCount()).isEqualTo(1);
    }

    private static Likes createLikes(Section section, User user) {
        return Likes.builder()
            .section(section)
            .user(user)
            .build();
    }

    private static Section createSection(User user, TemplateSection templateSection,
        Retrospective retrospective) {
        return Section.builder()
            .user(user)
            .content("test")
            .templateSection(templateSection)
            .likeCnt(0)
            .retrospective(retrospective)
            .build();
    }

    private static TemplateSection createTemplateSection(RetrospectiveTemplate kptTemplate) {
        return TemplateSection.builder()
            .sectionName("Keep")
            .sequence(0)
            .template(kptTemplate)
            .build();
    }

    @Test
    @DisplayName("특정 섹션 내용 수정")
    void updateSectionContentTest() {
        //given
        Long userId = 1L;
        User loginedUser = createUser();
        ReflectionTestUtils.setField(loginedUser, "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(loginedUser));

        Long sectionId = 1L;
        Section section = createSection(loginedUser);
        ReflectionTestUtils.setField(section, "id", sectionId);
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        //when
        EditSectionRequestDto request = new EditSectionRequestDto();
        ReflectionTestUtils.setField(request, "userId", userId);
        ReflectionTestUtils.setField(request, "sectionContent", request.getSectionContent());
        EditSectionResponseDto response = sectionService.updateSectionContent(
            sectionId, request);

        //then
        assertThat(response.getSectionId()).isEqualTo(sectionId);
        assertThat(response.getContent()).isEqualTo(request.getSectionContent());
    }

    @Test
    @DisplayName("회고 보드에 등록된 모든 섹션을 조회 할 수 있다.")
    void getSectionsTest() {
        //given
        Long teamId = 1L;
        Team createdTeam = createTeam();
        ReflectionTestUtils.setField(createdTeam, "id", teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(createdTeam));

        RetrospectiveTemplate createdTemplate = createTemplate();
        User createdUser = createUser();

        Long retrospectiveId = 1L;
        Retrospective createdRetrospective = createRetrospective(createdTemplate, createdUser, createdTeam);
        ReflectionTestUtils.setField(createdRetrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(Optional.of(createdRetrospective));

        TemplateSection createdTemplateSection = createTemplateSection(createdTemplate);

        Long sectionId = 1L;
        Section createdSection = createSection(createdUser, createdTemplateSection, createdRetrospective);
        ReflectionTestUtils.setField(createdSection, "id", sectionId);

        GetSectionsResponseDto response = new GetSectionsResponseDto(
            sectionId, createdUser.getUsername(), createdSection.getContent(), createdSection.getLikeCnt(),
            createdTemplateSection.getSectionName(), createdSection.getCreatedDate()
        );

        when(sectionRepository.getSections(retrospectiveId)).thenReturn(List.of(response));

        //when
        GetSectionsRequestDto request = new GetSectionsRequestDto();
        ReflectionTestUtils.setField(request, "retrospectiveId", retrospectiveId);
        ReflectionTestUtils.setField(request, "teamId", teamId);
        List<GetSectionsResponseDto> results = sectionService.getSections(request);

        //then
        assertThat(results.size()).isEqualTo(1);
        GetSectionsResponseDto result = results.get(0);
        assertThat(result.getSectionName()).isEqualTo(createdTemplateSection.getSectionName());
        assertThat(result.getSectionId()).isEqualTo(createdSection.getId());
        assertThat(result.getCreatedDate()).isEqualTo(createdSection.getCreatedDate());
        assertThat(result.getContent()).isEqualTo(createdSection.getContent());
        assertThat(result.getUsername()).isEqualTo(createdUser.getUsername());
        assertThat(result.getLikeCnt()).isEqualTo(createdSection.getLikeCnt());

    }

    @Test
    @DisplayName("다른 팀의 회고보드를 조회 할 수 없다.")
    void notSearchRetrospective() {
        //given
        Long teamId = 1L;
        Team createdTeam1 = createTeam();
        ReflectionTestUtils.setField(createdTeam1, "id", teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(createdTeam1));

        RetrospectiveTemplate createdTemplate = createTemplate();
        User createdUser = createUser();

        Team createdTeam2 = createTeam();
        ReflectionTestUtils.setField(createdTeam1, "id", 2L);

        Long retrospectiveId = 1L;
        Retrospective createdRetrospective = createRetrospective(createdTemplate ,createdUser, createdTeam2);
        ReflectionTestUtils.setField(createdRetrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(Optional.of(createdRetrospective));

        TemplateSection createdTemplateSection = createTemplateSection(createdTemplate);

        Long sectionId = 1L;
        Section createdSection = createSection(createdUser, createdTemplateSection, createdRetrospective);
        ReflectionTestUtils.setField(createdSection, "id", sectionId);

        //when
        GetSectionsRequestDto request = new GetSectionsRequestDto();
        ReflectionTestUtils.setField(request, "retrospectiveId", retrospectiveId);
        ReflectionTestUtils.setField(request, "teamId", teamId);

        //then
        assertThrows(ForbiddenAccessException.class, () -> sectionService.getSections(request));

    }

    private static Section createSection(User loginedUser) {
        return Section.builder()
            .user(loginedUser)
            .content("test")
            .likeCnt(0)
            .build();
    }

    private static Retrospective createRetrospective(RetrospectiveTemplate retrospectiveTemplate,
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

    private static RetrospectiveTemplate createTemplate() {
        return RetrospectiveTemplate.builder()
            .name("KPT")
            .build();
    }

    private static Team createTeam() {
        return Team.builder()
            .name("name")
            .build();
    }

    private static User createUser() {
        return User.builder()
            .username("test")
            .password("test")
            .phone("010-1234-1234")
            .email("test@naver.com")
            .build();
    }

    @Test
    @DisplayName("Action Items를 눌렀을 때 팀에 속한 모든 회원을 조회할 수 있다.")
    void getTeamMembers() {
        //given
        Long teamId = 1L;
        Team createdTeam = createTeam();
        ReflectionTestUtils.setField(createdTeam, "id", teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(createdTeam));

        Long userId = 1L;
        User createdUser = createUser();
        ReflectionTestUtils.setField(createdUser, "id", userId);

        Long userTeamId = 1L;
        UserTeam createdUserTeam = createUserTeam(createdUser, createdTeam);
        ReflectionTestUtils.setField(createdUserTeam, "id", userTeamId);

        GetTeamMembersResponseDto response = new GetTeamMembersResponseDto(userId, "test");
        when(userTeamRepository.findTeamMembers(teamId)).thenReturn(List.of(response));

        Long retrospectiveId = 1L;
        Retrospective createdRetrospective = createRetrospective(createTemplate(), createdUser,
            createdTeam);
        ReflectionTestUtils.setField(createdRetrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(Optional.of(createdRetrospective));

        //when
        GetTeamMembersRequestDto request = new GetTeamMembersRequestDto();
        ReflectionTestUtils.setField(request, "retrospectiveId", retrospectiveId);
        List<GetTeamMembersResponseDto> result = sectionService.getTeamMembers(teamId,
            request);

        //then
        assertThat(result.size()).isEqualTo(1);
        GetTeamMembersResponseDto searchUser = result.get(0);
        assertThat(searchUser.getUserId()).isEqualTo(userId);
        assertThat(searchUser.getUsername()).isEqualTo("test");

    }

    private static UserTeam createUserTeam(User createdUser, Team createdTeam) {
        return UserTeam.builder()
            .user(createdUser)
            .team(createdTeam)
            .build();
    }
}