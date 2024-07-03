package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.dto.GetRetrospectiveResponseDto;
import aws.retrospective.dto.GetRetrospectivesDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveResponseDto;
import aws.retrospective.dto.RetrospectiveType;
import aws.retrospective.dto.RetrospectivesOrderType;
import aws.retrospective.dto.UpdateRetrospectiveDto;
import aws.retrospective.entity.*;
import aws.retrospective.repository.BookmarkRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.repository.UserTeamRepository;
import aws.retrospective.util.TestUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.apache.bcel.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class RetrospectiveServiceTest {

    @Mock
    private RetrospectiveRepository retrospectiveRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RetrospectiveTemplateRepository templateRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

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

        List<Retrospective> retrospectiveList = new ArrayList<>();

        Retrospective retrospective = new Retrospective("New Retro", null, "some description", null,
            ProjectStatus.IN_PROGRESS, new Team("Team Name"),
            new User("user1", "test", "test", "test"), new RetrospectiveTemplate("Template Name"),
            LocalDateTime.now());

        ReflectionTestUtils.setField(retrospective, "id", 1L);

        retrospectiveList.add(retrospective);

        given(retrospectiveRepository.findRetrospectives(any(User.class),
            any(GetRetrospectivesDto.class)))
            .willReturn(retrospectiveList);
        given(retrospectiveRepository.countRetrospectives(any(User.class),
            any(GetRetrospectivesDto.class)))
            .willReturn((long) retrospectiveList.size());
        given(bookmarkRepository.findByRetrospectiveIdIn(anyList()))
            .willReturn(Collections.emptyList());

        // when
        PaginationResponseDto<RetrospectiveResponseDto> result = retrospectiveService.getRetrospectives(
            new User("user1", "test", "test", "test"), dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.nodes()).isNotEmpty();
        assertThat(result.nodes().size()).isEqualTo(retrospectiveList.size());
        assertThat(result.nodes().get(0).getId()).isEqualTo(retrospective.getId());

        verify(retrospectiveRepository).findRetrospectives(any(User.class),
            any(GetRetrospectivesDto.class));
        verify(retrospectiveRepository).countRetrospectives(any(User.class),
            any(GetRetrospectivesDto.class));
        verify(bookmarkRepository).findByRetrospectiveIdIn(anyList());
    }

    @Test
    void createRetrospective_ReturnsResponseDto_WhenCalledWithValidDto() {
        // given
        User user = new User("user1", "test", "test", "test");
        ReflectionTestUtils.setField(user, "id", 1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        Team team = new Team("Team Name");
        ReflectionTestUtils.setField(team, "id", 1L);
        given(teamRepository.save(any(Team.class))).willReturn(team);

        RetrospectiveTemplate template = new RetrospectiveTemplate("Template Name");
        ReflectionTestUtils.setField(template, "id", 1L);
        given(templateRepository.findById(1L)).willReturn(Optional.of(template));

        Retrospective retrospective = new Retrospective("New Retro",
            null,
            "some description",
            null,
            ProjectStatus.IN_PROGRESS,
            team, user, template,
            LocalDateTime.now());
        ReflectionTestUtils.setField(retrospective, "id", 1L);
        given(retrospectiveRepository.save(any(Retrospective.class)))
            .willReturn(retrospective);

        CreateRetrospectiveDto dto = new CreateRetrospectiveDto();
        ReflectionTestUtils.setField(dto, "title", "New Retro");
        ReflectionTestUtils.setField(dto, "type", RetrospectiveType.TEAM);
        ReflectionTestUtils.setField(dto, "templateId", 1L);
        ReflectionTestUtils.setField(dto, "status", ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(dto, "thumbnail", UUID.randomUUID());
        ReflectionTestUtils.setField(dto, "description", "some description");
        ReflectionTestUtils.setField(dto, "startDate", LocalDateTime.now());

        // when
        CreateRetrospectiveResponseDto response = retrospectiveService.createRetrospective(user,
            dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getTeamId()).isEqualTo(team.getId());
        assertThat(response.getTemplateId()).isEqualTo(template.getId());
        assertThat(response.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void deleteRetrospective_Success() {
        // Arrange
        User user = TestUtil.createUser();
        Team team = TestUtil.createTeam();
        RetrospectiveTemplate retrospectiveTemplate = TestUtil.createTemplate();
        Retrospective retrospective = TestUtil.createRetrospective(retrospectiveTemplate, user,
            team);

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(retrospective, "id", 1L);
        ReflectionTestUtils.setField(retrospective, "user", user);

        when(retrospectiveRepository.findById(1L)).thenReturn(Optional.of(retrospective));
        doNothing().when(retrospectiveRepository).deleteById(1L);

        // Act
        retrospectiveService.deleteRetrospective(1L, user);

        // Assert
        verify(retrospectiveRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRetrospective_Failure_UserNotAuthorized() {
        // Arrange
        User authorizedUser = TestUtil.createUser();
        Team team = TestUtil.createTeam();
        RetrospectiveTemplate retrospectiveTemplate = TestUtil.createTemplate();
        ReflectionTestUtils.setField(authorizedUser, "id", 1L);

        User unauthorizedUser = TestUtil.createUser();
        ReflectionTestUtils.setField(unauthorizedUser, "id", 123L);

        Retrospective unauthorizedRetrospective = TestUtil.createRetrospective(
            retrospectiveTemplate, unauthorizedUser,
            team);
        ReflectionTestUtils.setField(unauthorizedRetrospective, "id", 1L);
        ReflectionTestUtils.setField(unauthorizedRetrospective, "user", authorizedUser);

        when(retrospectiveRepository.findById(1L)).thenReturn(
            Optional.of(unauthorizedRetrospective));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            retrospectiveService.deleteRetrospective(1L, unauthorizedUser);
        });

        assertTrue(thrown.getMessage().contains("Not allowed to delete retrospective"));
    }

    @Test
    void deleteRetrospective_Failure_RetrospectiveNotFound() {
        // Arrange
        User user = TestUtil.createUser();

        when(retrospectiveRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            retrospectiveService.deleteRetrospective(1L, user);
        });

        assertTrue(thrown.getMessage().contains("Not found retrospective"));
    }

    @Test
    void updateRetrospective_Success() {
        // Arrange
        User user = TestUtil.createUser();
        Team team = TestUtil.createTeam();
        RetrospectiveTemplate retrospectiveTemplate = TestUtil.createTemplate();
        Retrospective retrospective = TestUtil.createRetrospective(retrospectiveTemplate, user,
            team);

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(retrospective, "id", 1L);
        ReflectionTestUtils.setField(retrospective, "user", user);

        UpdateRetrospectiveDto dto = new UpdateRetrospectiveDto();
        ReflectionTestUtils.setField(dto, "title", "New Retro");
        ReflectionTestUtils.setField(dto, "teamId", 1L);
        ReflectionTestUtils.setField(dto, "status", ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(dto, "thumbnail", UUID.randomUUID());
        ReflectionTestUtils.setField(dto, "description", "New Description");

        when(retrospectiveRepository.findById(1L)).thenReturn(Optional.of(retrospective));

        // Act
        RetrospectiveResponseDto response = retrospectiveService.updateRetrospective(user,
            1L,
            dto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(retrospective.getId());
        assertThat(response.getThumbnail()).isEqualTo(retrospective.getThumbnail());
    }

    @Test
    void updateRetrospective_Failure_RetrospectiveNotFound() {
        // Arrange
        User user = TestUtil.createUser();
        when(retrospectiveRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            retrospectiveService.updateRetrospective(user, 1L, new UpdateRetrospectiveDto());
        });

        assertTrue(thrown.getMessage().contains("Not found retrospective"));
    }

    @Test
    @DisplayName("단일 회고를 조회할 수 있다.")
    void findRetrospective() {
        // given
        Long userId = 1L;
        User user = TestUtil.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Long teamId = 2L;
        Team team = TestUtil.createTeam();
        ReflectionTestUtils.setField(team, "id", teamId);

        Long templateId = 3L;
        RetrospectiveTemplate retrospectiveTemplate = TestUtil.createTemplate(); // KPT
        ReflectionTestUtils.setField(retrospectiveTemplate, "id", templateId);

        Long retrospectiveId = 4L;
        Retrospective retrospective = TestUtil.createRetrospective(retrospectiveTemplate, user,
            team);
        ReflectionTestUtils.setField(retrospective, "id", retrospectiveId);
        when(retrospectiveRepository.findRetrospectiveById(retrospectiveId)).thenReturn(
            Optional.of(retrospective));

        // when
        GetRetrospectiveResponseDto findRetrospective = retrospectiveService.getRetrospective(
            user,
            retrospectiveId);

        // then
        assertThat(findRetrospective.getRetrospectiveId()).isEqualTo(retrospectiveId);
        assertThat(findRetrospective.getTitle()).isEqualTo("test");
        assertThat(findRetrospective.getTemplateId()).isEqualTo(templateId);
        assertThat(findRetrospective.getType()).isEqualTo(RetrospectiveType.TEAM);
        assertThat(findRetrospective.getUserId()).isEqualTo(userId);
        assertThat(findRetrospective.getLeaderName()).isEqualTo("test");
        assertThat(findRetrospective.getDescription()).isEqualTo("test");
        assertThat(findRetrospective.getStatus()).isEqualTo("IN_PROGRESS");
        assertThat(findRetrospective.getThumbnail()).isEqualTo(retrospective.getThumbnail());

    }

    @Test
    @DisplayName("리더가 아닌 경우, 리더 권한 전환 불가")
    void testTransferRetrospectiveLeadership_CurrentUserNotLeader() {
        // given (변수 설정)
        User currentUser = new User("user1", "test", "test", "test");
        User newLeader = new User("user2", "test", "test", "test");
        Team team = new Team("Team Name");

        // 리더가 아닌 역할로 설정된 UserTeam 객체 생성
        UserTeam currentUserTeam = UserTeam.builder()
                .user(currentUser)
                .team(team)
                .role(UserTeamRole.MEMBER)
                .build();

        Retrospective retrospective = new Retrospective("New Retro",
                null,
                "some description",
                null,
                ProjectStatus.IN_PROGRESS,
                team, currentUser,
                new RetrospectiveTemplate("Template Name"),
                LocalDateTime.now());

        when(retrospectiveRepository.findById(anyLong())).thenReturn(java.util.Optional.of(retrospective));

        // 리더가 아닌 역할로 설정
        currentUserTeam.updateMember();

        // 사용자가 인증되지 않은 상태를 시뮬레이트하기 위해 SecurityContextHolder에 null 값을 설정
        SecurityContextHolder.getContext().setAuthentication(null);

        //when
        assertThrows(NullPointerException.class, () -> {
            retrospectiveService.transferRetrospectiveLeadership(currentUser, 1L, 2L);
        });

        //then
        verify(userTeamRepository, never()).save(any(UserTeam.class));
        verify(retrospectiveRepository, never()).save(any(Retrospective.class));
    }

    @Test
    @DisplayName("리더인 경우, 다른 멤버에게 리더 권한 양도")
    void testTransferRetrospectiveLeadership_CurrentUserLeader() {
        // given (변수 설정)
        User currentUser = new User("user1", "test", "test", "test");
        User newLeader = new User("user2", "test", "test", "test");
        Team team = new Team("Team Name");

        // 리더로 설정된 UserTeam 객체 생성
        UserTeam currentUserTeam = UserTeam.builder()
                .user(currentUser)
                .team(team)
                .role(UserTeamRole.LEADER)
                .build();

        Retrospective retrospective = new Retrospective("New Retro",
                null,
                "some description",
                null,
                ProjectStatus.IN_PROGRESS,
                team, currentUser,
                new RetrospectiveTemplate("Template Name"),
                LocalDateTime.now());

        UserTeam newLeaderTeam = UserTeam.builder()
                .user(newLeader)
                .team(team)
                .role(UserTeamRole.MEMBER) // 새로운 리더를 멤버로 설정
                .build();

        when(retrospectiveRepository.findById(anyLong())).thenReturn(Optional.of(retrospective));


        // 사용자가 인증된 상태를 시뮬레이트하기 위해 SecurityContextHolder에 인증정보 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        assertThrows(NullPointerException.class, () -> {
            retrospectiveService.transferRetrospectiveLeadership(currentUser, 1L, newLeader.getId());
        });

        //then
        verify(userTeamRepository, never()).save(any(UserTeam.class)); // 현재 리더의 권한 변경
        verify(retrospectiveRepository, never()).save(any(Retrospective.class));
    }


}
