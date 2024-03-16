package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.DeleteSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
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
        Retrospective retrospective = createRetrospective(kptTemplate, team, user);

        Long sectionId = 1L;
        Section section = createSection(user, templateSection, retrospective);
        ReflectionTestUtils.setField(section, "id", sectionId);
        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        //when
        DeleteSectionResponseDto response = sectionService.deleteSection(sectionId);

        //then
        assertThat(response.getId()).isEqualTo(sectionId);
    }

    @Test
    @DisplayName("존재하지 않는 섹션을 삭제 시 예외가 발생한다.")
    void deleteSectionFailTest() {

        //given
        Long notExistSectionId = 1L;
        //when
        when(sectionRepository.findById(notExistSectionId)).thenThrow(NoSuchElementException.class);
        //then
        assertThrows(NoSuchElementException.class, () -> sectionService.deleteSection(notExistSectionId));
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
}