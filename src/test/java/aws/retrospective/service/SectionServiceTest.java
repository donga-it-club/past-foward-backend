package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.DeleteSectionResponseDto;
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
        Retrospective retrospective = createRetrospective(kptTemplate, team, user);
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
        doThrow(NoSuchElementException.class).when(sectionRepository).findById(2L); // 예외가 발생한다.
        DeleteSectionResponseDto response = sectionService.deleteSection(sectionId); // 예외가 발생하지 않는다.

        //then
        assertThrows(NoSuchElementException.class, () -> sectionService.deleteSection(2L));
        assertThat(response.getId()).isEqualTo(sectionId);
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

    private static Retrospective createRetrospective(RetrospectiveTemplate kptTemplate, Team team,
        User user) {
        return Retrospective.builder()
            .template(kptTemplate)
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