package aws.retrospective.service;

import aws.retrospective.dto.CreateRetrospectiveGroupDto;
import aws.retrospective.dto.CreateRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupsDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveGroupResponseDto;
import aws.retrospective.dto.RetrospectivesOrderType;
import aws.retrospective.dto.UpdateRetrospectiveGroupDto;
import aws.retrospective.entity.RetrospectiveGroup;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveGroupRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetrospectiveGroupService {

    private final RetrospectiveGroupRepository retrospectiveGroupRepository;
    private final BookmarkService bookmarkService;

    @Transactional(readOnly = true)
    public PaginationResponseDto<RetrospectiveGroupResponseDto> getRetrospectiveGroups(User user,
        GetRetrospectiveGroupsDto dto) {
        Sort sort = getSort(dto.getOrder());
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        Long userId = user.getId();

        Page<RetrospectiveGroup> page = retrospectiveGroupRepository.findByGroupIdAndUserId(dto.getGroupId(), userId, pageRequest);

        return PaginationResponseDto.fromPage(page,
            retrospectiveGroup -> RetrospectiveGroupResponseDto.of(retrospectiveGroup,
                hasBookmarksByUser(retrospectiveGroup, userId)));
    }

    private Sort getSort(RetrospectivesOrderType orderType) {
        if (orderType == RetrospectivesOrderType.OLDEST) {
            return Sort.by(Direction.ASC, "createdDate");
        }

        return Sort.by(Direction.DESC, "createdDate");
    }

    @Transactional(readOnly = true)
    public GetRetrospectiveGroupResponseDto getRetrospectiveGroup(User user, Long retrospectiveGroupId) {
        RetrospectiveGroup findRetrospectiveGroup = retrospectiveGroupRepository.findRetrospectiveGroupById(
            retrospectiveGroupId).orElseThrow(
            () -> new NoSuchElementException("Not found retrospectiveGroup: " + retrospectiveGroupId));

        return toResponse(findRetrospectiveGroup);
    }
    public GetRetrospectiveGroupResponseDto toResponse(RetrospectiveGroup findRetrospectiveGroup) {
        return new GetRetrospectiveGroupResponseDto(findRetrospectiveGroup.getId(),
            findRetrospectiveGroup.getTitle(),
            findRetrospectiveGroup.getUser().getId(),
            findRetrospectiveGroup.getDescription(),
            findRetrospectiveGroup.getStatus().name(),
            findRetrospectiveGroup.getThumbnail());
    }

    @Transactional
    public RetrospectiveGroupResponseDto updateRetrospectiveGroup(User user, Long retrospectiveGroupId,
        UpdateRetrospectiveGroupDto dto) {
        RetrospectiveGroup retrospectiveGroup = retrospectiveGroupRepository.findById(
            retrospectiveGroupId).orElseThrow(() -> new NoSuchElementException(
            "Not found retrospectiveGroup: " + retrospectiveGroupId));

        retrospectiveGroup.update(dto.getTitle(), dto.getStatus(), dto.getThumbnail(),
            dto.getDescription());

        boolean hasBookmarksByUser = hasBookmarksByUser(retrospectiveGroup, user.getId());

        return RetrospectiveGroupResponseDto.of(retrospectiveGroup, hasBookmarksByUser);
    }

    private boolean hasBookmarksByUser(RetrospectiveGroup retrospectiveGroup, Long userId) {
        return retrospectiveGroup.getBookmarks().stream()
            .anyMatch(bookmark -> bookmark.getUser().getId().equals(userId));
    }

    @Transactional
    public CreateRetrospectiveGroupResponseDto createRetrospectiveGroup(User user,
        CreateRetrospectiveGroupDto dto) {

        RetrospectiveGroup retrospectiveGroup = RetrospectiveGroup.builder().title(dto.getTitle())
            .status(dto.getStatus()).user(user)
            .thumbnail(dto.getThumbnail())
            .description(dto.getDescription()).build();

        RetrospectiveGroup savedRetrospectiveGroup = retrospectiveGroupRepository.save(
            retrospectiveGroup);

        return toResponseDto(savedRetrospectiveGroup);
        }

    private CreateRetrospectiveGroupResponseDto toResponseDto(RetrospectiveGroup retrospectiveGroup) {
        return CreateRetrospectiveGroupResponseDto.builder().id(retrospectiveGroup.getId())
            .title(retrospectiveGroup.getTitle())
            .userId(retrospectiveGroup.getUser().getId())
            .status(retrospectiveGroup.getStatus())
            .thumbnail(retrospectiveGroup.getThumbnail())
            .description(retrospectiveGroup.getDescription())
            .build();
    }

    @Transactional
    public void deleteRetrospectiveGroup(Long retrospectiveGroupId, User user) {
        RetrospectiveGroup retrospectiveGroup = retrospectiveGroupRepository.findById(
            retrospectiveGroupId).orElseThrow(() -> new NoSuchElementException(
            "Not found retrospectiveGroup: " + retrospectiveGroupId));

        if (!retrospectiveGroup.isOwnedByUser(user.getId())) {
            throw new IllegalArgumentException(
                "Not allowed to delete retrospectiveGroup: " + retrospectiveGroupId);
        }

        retrospectiveGroupRepository.deleteById(retrospectiveGroupId);
    }

    public boolean toggleBookmark(Long retrospectiveGroupId, User user) {
        return bookmarkService.toggleBookmark(user, retrospectiveGroupId);
    }
}
