package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aws.retrospective.dto.CreateRetrospectiveGroupDto;
import aws.retrospective.dto.CreateRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupsDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveGroupResponseDto;
import aws.retrospective.dto.RetrospectiveType;
import aws.retrospective.dto.RetrospectivesOrderType;
import aws.retrospective.dto.UpdateRetrospectiveGroupDto;
import aws.retrospective.entity.ProjectStatus;
import aws.retrospective.entity.RetrospectiveGroup;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveGroupRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.util.TestUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
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
public class RetrospectiveGroupServiceTest {

    @Mock
    private RetrospectiveGroupRepository retrospectiveGroupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RetrospectiveGroupService retrospectiveGroupService;

    @Test
    void getRetrospectiveGroups_ReturnsRetrospectiveGroupList_WhenCalledWithValidDto() {
        // given
        GetRetrospectiveGroupsDto dto = new GetRetrospectiveGroupsDto();
        dto.setPage(0);
        dto.setSize(10);
        dto.setOrder(RetrospectivesOrderType.OLDEST);
        dto.setKeyword("keyword");

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(),
            Sort.by(Sort.Direction.ASC, "createdDate"));
        List<RetrospectiveGroup> retrospectiveGroupList = new ArrayList<>();

        RetrospectiveGroup retrospectiveGroup = new RetrospectiveGroup("New Retro",
            null,
            "description",
            ProjectStatus.IN_PROGRESS,
            new User("user1", "test", "test", "test"));

        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);

        retrospectiveGroupList.add(retrospectiveGroup);
        Page<RetrospectiveGroup> retrospectiveGroupPage = new PageImpl<>(retrospectiveGroupList, pageable,
            retrospectiveGroupList.size());

        BDDMockito.given(retrospectiveGroupRepository.findAll(any(Specification.class), eq(pageable)))
            .willReturn(retrospectiveGroupPage);

        // when
        PaginationResponseDto<RetrospectiveGroupResponseDto> result = retrospectiveGroupService.getRetrospectiveGroup(
            new User("user1", "test", "test", "test"), dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.nodes()).isNotEmpty();
        assertThat(result.nodes().size()).isEqualTo(retrospectiveGroupList.size());
        assertThat(result.nodes().get(0).getId()).isEqualTo(retrospectiveGroup.getId());

        verify(retrospectiveGroupRepository).findAll(any(Specification.class), eq(pageable));

    }

    @Test
    void createRetrospectiveGroup_ReturnsResponseDto_WhenCalledWithValidDto() {
        //given
        User user = new User("user1", "test", "test", "test");
        ReflectionTestUtils.setField(user, "id", 1L);
        BDDMockito.given(userRepository.findById(1L)).willReturn(Optional.of(user));

        RetrospectiveGroup retrospectiveGroup = new RetrospectiveGroup("New Retro",
            null,
            "description",
            ProjectStatus.IN_PROGRESS,
            user);

        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);
        BDDMockito.given(retrospectiveGroupRepository.save(any(RetrospectiveGroup.class)))
            .willReturn(retrospectiveGroup);

        CreateRetrospectiveGroupDto dto = new CreateRetrospectiveGroupDto();
        ReflectionTestUtils.setField(dto, "title", "New Retro");
        ReflectionTestUtils.setField(dto, "status", ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(dto, "thumbnail", UUID.randomUUID());
        ReflectionTestUtils.setField(dto, "description", "some description");

        // when
        CreateRetrospectiveGroupResponseDto response = retrospectiveGroupService.createRetrospectiveGroup(user,
            dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void deleteRetrospectiveGroup_Success() {

        User user = TestUtil.createUser();
        RetrospectiveGroup retrospectiveGroup = TestUtil.createRetrospectiveGroup(user);

        ReflectionTestUtils.setField(user, "id", 1L);

        when(retrospectiveGroupRepository.findById(1L)).thenReturn(Optional.of(retrospectiveGroup));
        doNothing().when(retrospectiveGroupRepository).deleteById(1L);


        retrospectiveGroupService.deleteRetrospectiveGroup(1L, user);


        verify(retrospectiveGroupRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRetrospectiveGroup_Failure_UserNotAuthorized() {

        User authorizedUser = TestUtil.createUser();
        ReflectionTestUtils.setField(authorizedUser, "id", 1L);

        User unauthorizedUser = TestUtil.createUser();
        ReflectionTestUtils.setField(unauthorizedUser, "id", 123L);

        RetrospectiveGroup unauthorizedRetrospectiveGroup = TestUtil.createRetrospectiveGroup(unauthorizedUser);
        ReflectionTestUtils.setField(unauthorizedRetrospectiveGroup, "id", 1L);
        ReflectionTestUtils.setField(unauthorizedRetrospectiveGroup, "user", authorizedUser);

        when(retrospectiveGroupRepository.findById(1L)).thenReturn(
            Optional.of(unauthorizedRetrospectiveGroup));


        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            retrospectiveGroupService.deleteRetrospectiveGroup(1L, unauthorizedUser);
        });

        assertTrue(thrown.getMessage().contains("Not allowed to delete retrospectiveGroup"));
    }

    @Test
    void updateRetrospectiveGroup_Success() {
        User user = TestUtil.createUser();
        RetrospectiveGroup retrospectiveGroup = TestUtil.createRetrospectiveGroup(user);

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(retrospectiveGroup, "id", 1L);
        ReflectionTestUtils.setField(retrospectiveGroup, "user", user);

        UpdateRetrospectiveGroupDto dto = new UpdateRetrospectiveGroupDto();
        ReflectionTestUtils.setField(dto, "title", "New Retro");
        ReflectionTestUtils.setField(dto, "status", ProjectStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(dto, "thumbnail", UUID.randomUUID());
        ReflectionTestUtils.setField(dto, "description", "New Description");

        when(retrospectiveGroupRepository.findById(1L)).thenReturn(Optional.of(retrospectiveGroup));

        RetrospectiveGroupResponseDto response = retrospectiveGroupService.updateRetrospectiveGroup(
            user, 1L, dto);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(retrospectiveGroup.getId());
        assertThat(response.getThumbnail()).isEqualTo(retrospectiveGroup.getThumbnail());
    }

    @Test
    void updateRetrospectiveGroup_Failure_RetrospectiveNotFound() {
        // Arrange
        User user = TestUtil.createUser();
        when(retrospectiveGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            retrospectiveGroupService.updateRetrospectiveGroup(user, 1L, new UpdateRetrospectiveGroupDto());
        });

        assertTrue(thrown.getMessage().contains("Not found retrospectiveGroup"));
    }
}
