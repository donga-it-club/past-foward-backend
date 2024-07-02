package aws.retrospective.service;

import aws.retrospective.dto.*;
import aws.retrospective.entity.*;
import aws.retrospective.repository.RetrospectiveGroupRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.specification.RetrospectiveGroupSpecification;
import aws.retrospective.util.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RetrospectiveGroupServiceTest {

    @Mock
    private RetrospectiveGroupRepository retrospectiveGroupRepository;

    @Mock
    private RetrospectiveRepository retrospectiveRepository;

    @InjectMocks
    private RetrospectiveGroupService retrospectiveGroupService;

    @Test
    @DisplayName("회고 그룹 등록 API")
    void createRetrospectiveGroupTest() {
        // given
        User user = new User("user1", "test", "test", "test",false);
        ReflectionTestUtils.setField(user, "id", 1L);

        RetrospectiveGroup retrospectiveGroup = new RetrospectiveGroup("New Retro Group",
            null,
            "description",
            user,
            ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);
        given(retrospectiveGroupRepository.save(any(RetrospectiveGroup.class)))
            .willReturn(retrospectiveGroup);

        CreateRetrospectiveGroupDto dto = new CreateRetrospectiveGroupDto();
        ReflectionTestUtils.setField(dto, "title", "New Retro Group");
        ReflectionTestUtils.setField(dto, "status", ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(dto, "thumbnail", UUID.randomUUID());
        ReflectionTestUtils.setField(dto, "description", "description");

        // when
        CreateRetrospectiveGroupResponseDto response = retrospectiveGroupService.createRetrospectiveGroup(user, dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    @DisplayName("모든 회고 그룹 조희 API")
    void getRetrospectiveGroupsTest() {
        // given
        GetRetrospectiveGroupsDto dto = new GetRetrospectiveGroupsDto();
        dto.setPage(0);
        dto.setSize(10);
        dto.setKeyword("keyword");

        Sort sort = Sort.by("createdDate").descending();
        PageRequest pageable = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        Long userId = 1L;
      
        User user = new User("user1", "test", "test", "test",false);
        ReflectionTestUtils.setField(user, "id", userId);

        Specification<RetrospectiveGroup> spec = Specification.where(
                RetrospectiveGroupSpecification.withKeyword(dto.getKeyword()))
            .and(RetrospectiveGroupSpecification.withUserId(userId))
            .and(RetrospectiveGroupSpecification.withStatus(dto.getStatus()))
            .and(RetrospectiveGroupSpecification.withBookmark(dto.getIsBookmarked(), userId));

        List<RetrospectiveGroup> retrospectiveGroupList = new ArrayList<>();
        RetrospectiveGroup retrospectiveGroup = new RetrospectiveGroup("New Retro",
            null,
            "some description",
            user,
            ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);
        retrospectiveGroupList.add(retrospectiveGroup);
        Page<RetrospectiveGroup> retrospectiveGroupPage = new PageImpl<>(retrospectiveGroupList, pageable,
            retrospectiveGroupList.size());

        // ArgumentCaptor를 사용하여 Specification을 캡처
        ArgumentCaptor<Specification> specCaptor = ArgumentCaptor.forClass(Specification.class);
        given(retrospectiveGroupRepository.findAll(specCaptor.capture(), eq(pageable)))
            .willReturn(retrospectiveGroupPage);

        // when
        PaginationResponseDto<RetrospectiveGroupResponseDto> result = retrospectiveGroupService.getRetrospectiveGroups(
            user, dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.nodes()).isNotEmpty();
        assertThat(result.nodes().size()).isEqualTo(retrospectiveGroupList.size());
        assertThat(result.nodes().get(0).getId()).isEqualTo(retrospectiveGroup.getId());
    }

    @Test
    @DisplayName("회고 그룹 상세 수정 API")
    void updateRetrospectiveGroupTest() {
        // given
        User user = TestUtil.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        RetrospectiveGroup retrospectiveGroup = TestUtil.createRetrospectiveGroup(user);
        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);
        ReflectionTestUtils.setField(retrospectiveGroup, "user", user);

        UpdateRetrospectiveGroupDto dto = new UpdateRetrospectiveGroupDto();
        ReflectionTestUtils.setField(dto, "title", "New Retro");
        ReflectionTestUtils.setField(dto, "status", ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(dto, "thumbnail", UUID.randomUUID());
        ReflectionTestUtils.setField(dto, "description", "New Description");

        when(retrospectiveGroupRepository.findById(retrospectiveGroup.getId())).thenReturn(
            Optional.of(retrospectiveGroup));

        // when
        RetrospectiveGroupResponseDto response = retrospectiveGroupService.updateRetrospectiveGroup(user,
            1L,
            dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(retrospectiveGroup.getId());
        assertThat(response.getThumbnail()).isEqualTo(retrospectiveGroup.getThumbnail());
    }

    @Test
    @DisplayName("회고 그룹 보드 수정 API")
    void updateRetrospectiveGroupBoardsTest() {
        // given
        User user = TestUtil.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        RetrospectiveGroup retrospectiveGroup = TestUtil.createRetrospectiveGroup(user);
        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);
        ReflectionTestUtils.setField(retrospectiveGroup, "user", user);

        UpdateRetrospectiveGroupBoardsDto dto = new UpdateRetrospectiveGroupBoardsDto();

        // 새로운 회고 ID 리스트를 dto에 설정
        List<Long> retrospectiveIds = List.of(2L, 3L);
        ReflectionTestUtils.setField(dto, "retrospectiveIds", retrospectiveIds);

        // 새로 추가될 회고 객체 모킹
        Retrospective retrospective1 = mock(Retrospective.class);
        Retrospective retrospective2 = mock(Retrospective.class);
        // 모킹된 회고 객체에 ID 설정
        when(retrospective1.getId()).thenReturn(2L);
        when(retrospective2.getId()).thenReturn(3L);
        when(retrospectiveGroupRepository.findById(1L)).thenReturn(Optional.of(retrospectiveGroup));
        // 회고 리포지토리 모킹: ID 리스트로 회고들을 찾았을 때 미리 생성한 회고 객체 리스트 반환
        when(retrospectiveRepository.findAllById(retrospectiveIds)).thenReturn(List.of(retrospective1, retrospective2));

        // when
        RetrospectiveGroupResponseDto response = retrospectiveGroupService.updateRetrospectiveGroupBoards(user,
            1L,
            dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(retrospectiveGroup.getId());
        assertThat(response.getThumbnail()).isEqualTo(retrospectiveGroup.getThumbnail());
        // 회고 그룹에 새로 추가된 회고들의 개수가 2개인지 확인
        assertThat(retrospectiveGroup.getRetrospectives()).hasSize(2);
        // 회고 그룹에 새로 추가된 회고들의 ID가 설정한 ID와 동일한지 확인
        assertThat(retrospectiveGroup.getRetrospectives()).extracting("id").containsExactlyInAnyOrder(2L, 3L);
    }

    @Test
    void updateRetrospectiveGroup_Fails_WhenGroupNotFound() {
        // given
        User user = new User("user1", "test", "test", "test",false);
        when(retrospectiveGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when / then
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> retrospectiveGroupService.updateRetrospectiveGroup(user, 1L, new UpdateRetrospectiveGroupDto()));

        assertThat(thrown.getMessage()).contains("Not found retrospective group: 1");
    }

    @Test
    @DisplayName("회고 그룹 삭제 성공 API")
    void deleteRetrospectiveGroupSuccess() {
        //given
        User user = TestUtil.createUser();
        RetrospectiveGroup retrospectiveGroup = TestUtil.createRetrospectiveGroup(user);

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);
        ReflectionTestUtils.setField(retrospectiveGroup, "user", user);

        when(retrospectiveGroupRepository.findById(1L)).thenReturn(Optional.of(retrospectiveGroup));
        doNothing().when(retrospectiveGroupRepository).deleteById(1L);

        //when
        retrospectiveGroupService.deleteRetrospectiveGroup(1L, user);

        //then
        verify(retrospectiveGroupRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("회고 그룹 삭제 실패 API")
    void deleteRetrospectiveGroup_Failure_UserNotAuthorized() {

        // Arrange
        User authorizedUser = TestUtil.createUser();
        ReflectionTestUtils.setField(authorizedUser, "id", 1L);

        User unauthorizedUser = TestUtil.createUser();
        ReflectionTestUtils.setField(unauthorizedUser, "id", 123L);

        RetrospectiveGroup unauthorizedRetrospectiveGroup = TestUtil.createRetrospectiveGroup(
            unauthorizedUser);
        ReflectionTestUtils.setField(unauthorizedRetrospectiveGroup, "id", 1L);
        ReflectionTestUtils.setField(unauthorizedRetrospectiveGroup, "user", authorizedUser);

        when(retrospectiveGroupRepository.findById(1L)).thenReturn(
            Optional.of(unauthorizedRetrospectiveGroup));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> retrospectiveGroupService.deleteRetrospectiveGroup(1L, unauthorizedUser));

        assertTrue(!thrown.getMessage().contains("Not allowed to delete retrospective group"));
    }

    @Test
    void deleteRetrospectiveGroup_Fails_WhenGroupNotFound() {
        // given
        User user = new User("user1", "test", "test", "test",false);
        when(retrospectiveGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when / then
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> retrospectiveGroupService.deleteRetrospectiveGroup(1L, user));

        assertThat(thrown.getMessage()).contains("Not found retrospective group: 1");
    }

}
