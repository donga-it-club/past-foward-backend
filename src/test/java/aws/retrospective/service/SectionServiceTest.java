package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactionalgit ence(0)
            .build();
        when(templateSectionRepository.findById(templateSectionId)).thenReturn(
            Optional.of(templateSection));

        Long retrospectiveId = 1L;
        Retrospective retrospective = createRetrospective(createTeam(), user,
            retrospectiveTemplate);
        when(retrospectiveRepository.findById(retrospectiveId)).thenReturn(
            Optional.of(retrospective));

        CreateSectionDto request = new CreateSectionDto();
        ReflectionTestUtils.setField(request, "userId", userId);
        ReflectionTestUtils.setField(request, "retrospectiveId", retrospectiveId);
        ReflectionTestUtils.setField(request, "templateSectionId", templateSectionId);
        ReflectionTestUtils.setField(request, "sectionContent", "section content");

        //when
        CreateSectionResponseDto response = sectionService.createSection(request);

        //then
        assertThat(response.getSectionContent()).isEqualTo(request.getSectionContent());
        assertThat(response.getRetrospectiveId()).isEqualTo(retrospective.getId());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getUserId()).isEqualTo(user.getId());
    }

    private static Retrospective createRetrospective(Team team, User user,
        RetrospectiveTemplate retrospectiveTemplate) {
        return Retrospective.builder()
            .team(team)
            .user(user)
            .title("회고 제목")
            .status(ProjectStatus.IN_PROGRESS)
            .template(retrospectiveTemplate)
            .build();
    }

    private static Team createTeam() {
        return Team.builder()
            .name("팀A")
            .build();
    }

    private static RetrospectiveTemplate createRetrospectiveTemplate() {
        return RetrospectiveTemplate.builder()
            .name("KPT")
            .build();
    }

    private static User createUser() {
        return User.builder()
            .email("test")
            .phone("test")
            .password("test")
            .username("test")
            .build();
    }

}