package aws.retrospective.service;

import aws.retrospective.dto.CreateRetrospectiveGroupDto;
import aws.retrospective.dto.CreateRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupResponseDto;
import aws.retrospective.dto.GetRetrospectiveGroupsDto;
import aws.retrospective.dto.PaginationResponseDto;
import aws.retrospective.dto.RetrospectiveGroupResponseDto;
import aws.retrospective.dto.UpdateRetrospectiveGroupBoardsDto;
import aws.retrospective.dto.UpdateRetrospectiveGroupDto;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveGroup;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveGroupRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.specification.RetrospectiveGroupSpecification;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetrospectiveGroupService {

    private final RetrospectiveGroupRepository retrospectiveGroupRepository;
    private final RetrospectiveRepository retrospectiveRepository;

    // 회고 그룹 생성
    @Transactional
    public CreateRetrospectiveGroupResponseDto createRetrospectiveGroup(User user,
                                                                        CreateRetrospectiveGroupDto dto) {

        RetrospectiveGroup retrospectiveGroup = RetrospectiveGroup.builder()
                .title(dto.getTitle())
                .status(dto.getStatus())
                .thumbnail(dto.getThumbnail())
                .user(user)
                .description(dto.getDescription()).build();

        RetrospectiveGroup savedRetrospectiveGroup = retrospectiveGroupRepository.save(retrospectiveGroup);

        return toResponseDto(savedRetrospectiveGroup);
    }

    private CreateRetrospectiveGroupResponseDto toResponseDto(RetrospectiveGroup retrospectiveGroup) {
        return CreateRetrospectiveGroupResponseDto.builder().id(retrospectiveGroup.getId())
                .title(retrospectiveGroup.getTitle())
                .userId(retrospectiveGroup.getUser().getId())
                .status(retrospectiveGroup.getStatus()).thumbnail(retrospectiveGroup.getThumbnail())
                .description(retrospectiveGroup.getDescription())
                .build();
    }

    // 회고 그룹 조회
    @Transactional(readOnly = true)
    public PaginationResponseDto<RetrospectiveGroupResponseDto> getRetrospectiveGroups(User user,
                                                                                       GetRetrospectiveGroupsDto dto) {
        Sort sort = getSort();
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        Long userId = user.getId();

        Specification<RetrospectiveGroup> spec = Specification.where(
                        RetrospectiveGroupSpecification.withKeyword(dto.getKeyword()))
                .and(RetrospectiveGroupSpecification.withUserId(userId))
                .and(RetrospectiveGroupSpecification.withStatus(dto.getStatus()))
                .and(RetrospectiveGroupSpecification.withBookmark(dto.getIsBookmarked(), userId));

        Page<RetrospectiveGroup> page = retrospectiveGroupRepository.findAll(spec, pageRequest);

        return PaginationResponseDto.fromPage(page,
                retrospectiveGroup -> RetrospectiveGroupResponseDto.of(retrospectiveGroup,
                        hasBookmarksByUser(retrospectiveGroup, userId)));
    }

    private Sort getSort() {
        return Sort.by(Direction.DESC, "createdDate");
    }

    @Transactional(readOnly = true)
    public GetRetrospectiveGroupResponseDto getRetrospectiveGroup(User user, Long retrospectiveGroupId) {
        RetrospectiveGroup findRetrospectiveGroup = retrospectiveGroupRepository.findRetrospectiveGroupById(
                retrospectiveGroupId).orElseThrow(
                () -> new NoSuchElementException("Not found retrospective group: " + retrospectiveGroupId));

        return toResponse(findRetrospectiveGroup);
    }

    private GetRetrospectiveGroupResponseDto toResponse(RetrospectiveGroup findRetrospectiveGroup) {
        return new GetRetrospectiveGroupResponseDto(findRetrospectiveGroup.getId(),
                findRetrospectiveGroup.getTitle(), findRetrospectiveGroup.getUser().getId(),
                findRetrospectiveGroup.getUser().getUsername(), findRetrospectiveGroup.getDescription(),
                findRetrospectiveGroup.getThumbnail(), findRetrospectiveGroup.getStatus().name());
    }

    // 회고 그룹 보드 수정
    @Transactional
    public RetrospectiveGroupResponseDto updateRetrospectiveGroupBoards(User user, Long retrospectiveGroupId,
                                                                        UpdateRetrospectiveGroupBoardsDto dto) {
        RetrospectiveGroup retrospectiveGroup = retrospectiveGroupRepository.findById(retrospectiveGroupId).orElseThrow(
                () -> new NoSuchElementException("Not found retrospective group: " + retrospectiveGroupId));

        // 기존 회고 제거
        retrospectiveGroup.getRetrospectives().clear();

        // 새 회고 추가
        if (dto.getRetrospectiveIds() != null && !dto.getRetrospectiveIds().isEmpty()) {
            List<Retrospective> retrospectives = retrospectiveRepository.findAllById(dto.getRetrospectiveIds());
            for (Retrospective retrospective : retrospectives) {
                retrospectiveGroup.addRetrospective(retrospective);
            }
        }

        // 회고 그룹 저장
        retrospectiveGroupRepository.save(retrospectiveGroup);

        boolean hasBookmarksByUser = hasBookmarksByUser(retrospectiveGroup, user.getId());

        return RetrospectiveGroupResponseDto.of(retrospectiveGroup, hasBookmarksByUser);
    }

    //회고 그룹 상세 수정
    @Transactional
    public RetrospectiveGroupResponseDto updateRetrospectiveGroup(User user, Long retrospectiveGroupId,
                                                                  UpdateRetrospectiveGroupDto dto) {
        RetrospectiveGroup retrospectiveGroup = retrospectiveGroupRepository.findById(retrospectiveGroupId).orElseThrow(
                () -> new NoSuchElementException("Not found retrospective group: " + retrospectiveGroupId));

        retrospectiveGroup.update(dto.getTitle(), dto.getStatus(), dto.getThumbnail(),
                dto.getDescription());

        // 회고 그룹 저장
        retrospectiveGroupRepository.save(retrospectiveGroup);

        boolean hasBookmarksByUser = hasBookmarksByUser(retrospectiveGroup, user.getId());

        return RetrospectiveGroupResponseDto.of(retrospectiveGroup, hasBookmarksByUser);
    }

    //회고 그룹 삭제
    @Transactional
    public void deleteRetrospectiveGroup(Long retrospectiveGroupId, User user) {
        RetrospectiveGroup retrospectiveGroup = retrospectiveGroupRepository.findById(retrospectiveGroupId).orElseThrow(

                () -> new NoSuchElementException("Not found retrospective group: " + retrospectiveGroupId));




        if (!retrospectiveGroup.isOwnedByUser(user.getId())) {

            throw new IllegalArgumentException(

                    "Not allowed to delete retrospective: " + retrospectiveGroupId);

        }




        retrospectiveGroupRepository.deleteById(retrospectiveGroupId);

    }




    private boolean hasBookmarksByUser(RetrospectiveGroup retrospectiveGroup, Long userId) {

        return retrospectiveGroup.getBookmarks().stream()

                .anyMatch(bookmark -> bookmark.getUser().getId().equals(userId));

    }







}
