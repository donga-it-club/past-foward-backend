package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.dto.GetRetrospectivesDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveResponseDto;
import aws.retrospective.dto.RetrospectivesOrderType;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class RetrospectiveServiceTest {

    @Mock
    private RetrospectiveRepository retrospectiveRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RetrospectiveTemplateRepository templateRepository;

    @InjectMocks
    private RetrospectiveService retrospectiveService;

    @Test
    void getRetrospectives_ReturnsRetrospectiveList_WhenCalledWithValidDto() {
        // given
        GetRetrospectivesDto dto = new GetRetrospectivesDto();
        dto.setPage(0);
        dto.setSize(10);
        dto.setOrder(RetrospectivesOrderType.OLDEST);
        dto.setKeyword("keyword");

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(),
            Sort.by(Sort.Direction.ASC, "createdDate"));
        List<Retrospective> retrospectiveList = new ArrayList<>();

        Retrospective retrospective = new Retrospective("New Retro",
            ProjectStatus.IN_PROGRESS,
            new Team("Team Name"), new User("user1", "test", "test", "test"),
            new RetrospectiveTemplate("Template Name"));

        ReflectionTestUtils.setField(retrospective, "id", 1L);

        retrospectiveList.add(retrospective);
        Page<Retrospective> retrospectivePage = new PageImpl<>(retrospectiveList, pageable,
            retrospectiveList.size());

        BDDMockito.given(retrospectiveRepository.findAll(any(Specification.class), eq(pageable)))
            .willReturn(retrospectivePage);

        // when
        PaginationResponseDto<RetrospectiveResponseDto> result = retrospectiveService.getRetrospectives(
            dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.nodes()).isNotEmpty();
        assertThat(result.nodes().size()).isEqualTo(retrospectiveList.size());
        assertThat(result.nodes().get(0).getId()).isEqualTo(retrospective.getId());

        verify(retrospectiveRepository).findAll(any(Specification.class), eq(pageable));

    }


    @Test
    void createRetrospective_ReturnsResponseDto_WhenCalledWithValidDto() {
        // given
        User user = new User("user1", "test", "test", "test");
        ReflectionTestUtils.setField(user, "id", 1L);
        BDDMockito.given(userRepository.findById(1L)).willReturn(Optional.of(user));

        Team team = new Team("Team Name");
        ReflectionTestUtils.setField(team, "id", 1L);
        BDDMockito.given(teamRepository.findById(1L)).willReturn(Optional.of(team));

        RetrospectiveTemplate template = new RetrospectiveTemplate("Template Name");
        ReflectionTestUtils.setField(template, "id", 1L);
        BDDMockito.given(templateRepository.findById(1L)).willReturn(Optional.of(template));

        Retrospective retrospective = new Retrospective("New Retro",
            ProjectStatus.IN_PROGRESS,
            team, user, template);

        ReflectionTestUtils.setField(retrospective, "id", 1L);
        BDDMockito.given(retrospectiveRepository.save(any(Retrospective.class)))
            .willReturn(retrospective);

        CreateRetrospectiveDto dto = new CreateRetrospectiveDto();
        ReflectionTestUtils.setField(dto, "title", "New Retro");
        ReflectionTestUtils.setField(dto, "teamId", 1L);
        ReflectionTestUtils.setField(dto, "userId", 1L);
        ReflectionTestUtils.setField(dto, "templateId", 1L);

        // when
        CreateRetrospectiveResponseDto response = retrospectiveService.createRetrospective(dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getTeamId()).isEqualTo(team.getId());
        assertThat(response.getTemplateId()).isEqualTo(template.getId());
    }

}
