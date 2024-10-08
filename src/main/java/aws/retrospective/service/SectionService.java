package aws.retrospective.service;

import aws.retrospective.dto.AssignKudosRequestDto;
import aws.retrospective.dto.AssignKudosResponseDto;
import aws.retrospective.dto.AssignUserRequestDto;
import aws.retrospective.dto.CreateSectionRequest;
import aws.retrospective.dto.CreateSectionResponse;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.GetCommentDto;
import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.entity.ActionItem;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.KudosTarget;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Notification;
import aws.retrospective.entity.NotificationType;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.event.SectionCacheDeleteEvent;
import aws.retrospective.factory.SectionFactory;
import aws.retrospective.repository.ActionItemRepository;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.KudosTargetRepository;
import aws.retrospective.repository.LikesRepository;
import aws.retrospective.repository.NotificationRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionCacheRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionService {

    private final SectionRepository sectionRepository;
    private final RetrospectiveRepository retrospectiveRepository;
    private final TemplateSectionRepository templateSectionRepository;
    private final LikesRepository likesRepository;
    private final TeamRepository teamRepository;
    private final ActionItemRepository actionItemRepository;
    private final UserRepository userRepository;
    private final KudosTargetRepository kudosRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SectionFactory sectionFactory;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateSectionResponse createSection(User user, CreateSectionRequest request) {
        Retrospective retrospective = findRetrospectiveById(request.getRetrospectiveId());
        TemplateSection templateSection = findTemplateSectionById(request.getTemplateSectionId());

        validationTemplateSection(retrospective, templateSection);

        Section section = sectionFactory.createSection(request.getSectionContent(), retrospective,
            templateSection, user);
        Section savedSection = sectionRepository.save(section);

        eventPublisher.publishEvent(new SectionCacheDeleteEvent(request.getRetrospectiveId()));

        return CreateSectionResponse.of(savedSection);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = SectionCacheRepository.CACHE_KEY, key = "#request.retrospectiveId", cacheResolver = "customCacheResolver")
    public List<GetSectionsResponseDto> getSections(GetSectionsRequestDto request) {
        Retrospective retrospective = findRetrospectiveById(request.getRetrospectiveId());

        // 회고 카드와 댓글을 분리해서 조회
        List<GetSectionsResponseDto> sections = getSectionsWithoutComments(retrospective.getId());
        List<Long> sectionIds = extractSectionIds(sections);
        List<Comment> comments = getCommentsBy(sectionIds);

        // 회고 카드와 댓글을 매핑
        Map<Long, List<GetCommentDto>> sectionCommentsMap = createGroupCommentsBySectionId(comments);
        addCommentsToSection(sections, sectionCommentsMap);

        return sections;
    }

    // 회고 카드 수정 API
    @Transactional
    public EditSectionResponseDto updateSectionContent(User user, Long sectionId,
        EditSectionRequestDto request) {
        Section findSection = getSection(sectionId);

        /**
         * 회고 카드 작성자와 현재 사용자가 일치하는지 확인한다.
         * 일치하지 않으면 예외를 발생시킨다.
         */
        validateSameUser(findSection, user);

        // 회고 카드 내용 수정
        findSection.updateSectionContent(request.getSectionContent());
        eventPublisher.publishEvent(new SectionCacheDeleteEvent(findSection.getRetrospective().getId()));
        return convertUpdateSectionResponseDto(findSection.getId(), findSection.getContent());
    }

    // 회고 카드 좋아요 API
    @Transactional
    public IncreaseSectionLikesResponseDto increaseSectionLikes(Long sectionId, User user) {
        // 회고 카드 조회
        Section findSection = getSection(sectionId);
        // 사용자가 해당 회고 카드에 좋아요를 눌렀는지 확인한다.
        Optional<Likes> findLikes = likesRepository.findByUserAndSection(user, findSection);

        // 좋아요를 누른적이 없을 때는 좋아요 횟수를 증가시킨다.
        if (findLikes.isEmpty()) {
            Likes createLikes = createLikes(findSection, user);
            likesRepository.save(createLikes);
            findSection.increaseSectionLikes(); // 좋아요 기록 저장

            // 댓글 알림 생성
            Notification notification = createNotification(findSection,
                findSection.getRetrospective(),
                user, findSection.getUser(), createLikes);
            notificationRepository.save(notification);
        } else {
            Likes likes = findLikes.get();
            deleteNotification(likes); // 기존의 알림을 삭제
            likesRepository.delete(likes); // 좋아요 기록 삭제
            findSection.cancelSectionLikes(); // 좋아요 취소
        }

        eventPublisher.publishEvent(new SectionCacheDeleteEvent(findSection.getRetrospective().getId()));
        return convertIncreaseSectionLikesResponseDto(findSection);
    }

    // Action Items 사용자 지정
    @Transactional
    public void assignUserToActionItem(AssignUserRequestDto request) {
        Team team = getTeam(request.getTeamId());
        Retrospective retrospective = getRetrospective(request.getRetrospectiveId());
        Section section = getSection(request.getSectionId());

        // Action Items 유형인지 확인한다.
        validateActionItems(section);

        // Action Item을 가져온다.
        ActionItem actionItem = getActionItem(section);
        // Action Item에 지정할 사용자를 조회한다.
        User assignUser = getAssignUser(request);

        /**
         * Action Item이 없을 때는 새로 생성하고, 있을 때는 사용자를 지정한다.
         */
        if (actionItem == null) {
            // Action Item 사용자 지정
            assignActionItem(assignUser, team, section, retrospective);
        } else {
            // 기존에 등록된 Action Item에 새로운 사용자를 지정한다.
            actionItem.assignUser(assignUser);
        }

        eventPublisher.publishEvent(new SectionCacheDeleteEvent(section.getRetrospective().getId()));
    }

    // 회고카드 삭제
    @Transactional
    public void deleteSection(Long sectionId, User user) {
        Section findSection = getSection(sectionId);

        /**
         * 회고 카드 작성자와 현재 사용자가 일치하는지 확인한다.
         * 일치하지 않으면 예외를 발생시킨다.
         */
        validateSameUser(findSection, user);
        eventPublisher.publishEvent(new SectionCacheDeleteEvent(findSection.getRetrospective().getId()));
        deleteSection(findSection);
    }

    @Transactional
    public AssignKudosResponseDto assignKudos(Long sectionId, AssignKudosRequestDto request) {
        Section section = getSection(sectionId); // Kudos Section

        // Kudos 유형에만 칭창할 사람을 지정8할 수 있다.
        validateKudosTemplate(sectionId, section);

        User targetUser = getUser(request.getUserId()); // 칭찬 대상 조회
        KudosTarget findKudosTarget = getKudosSection(section); // DB에 저장된 Kudos 정보 조회

        /**
         * Kudos 정보가 없을 때는 새로 생성하고, 있을 때는 사용자를 지정(변경)한다.
         */
        if (findKudosTarget == null) {
            KudosTarget createNewKudosTarget = assignKudos(section, targetUser);
            return convertAssignKudosResponse(createNewKudosTarget);
        }

        findKudosTarget.assignUser(targetUser);
        eventPublisher.publishEvent(new SectionCacheDeleteEvent(section.getRetrospective().getId()));
        return convertAssignKudosResponse(findKudosTarget);
    }

    private static void validateKudosTemplate(Long sectionId, Section section) {
        if (section.isNotKudosTemplate()) {
            throw new IllegalArgumentException(
                "Kudos Section이 아닙니다. id : " + sectionId);
        }
    }

    private Retrospective getRetrospective(Long retrospectiveId) {
        return retrospectiveRepository.findById(retrospectiveId)
            .orElseThrow(() -> new NoSuchElementException("회고보드가 조회되지 않습니다."));
    }

    private Section getSection(Long sectionId) {
        return sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("회고 카드가 조회되지 않습니다."));
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new NoSuchElementException("Not Found Team id : " + teamId));
    }

    private static ActionItem createActionItem(User user, Team team, Section section,
        Retrospective retrospective) {
        return ActionItem.builder().user(user).team(team).section(section)
            .retrospective(retrospective).build();
    }

    private User getAssignUser(AssignUserRequestDto request) {
        return userRepository.findById(request.getUserId()).orElseThrow(
            () -> new NoSuchElementException("Not Found User Id : " + request.getUserId()));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(
                () -> new NoSuchElementException("Not Found User Id : " + userId));
    }

    private KudosTarget assignKudos(Section section, User user) {
        return kudosRepository.save(KudosTarget.createKudosTarget(section, user));
    }

    private boolean validateSameUser(Section section, User user) {
        return section.isNotSameUser(user);
    }

    private static EditSectionResponseDto convertUpdateSectionResponseDto(Long sectionId,
        String sectionContent) {
        return EditSectionResponseDto.builder().sectionId(sectionId)
            .content(sectionContent).build();
    }

    public void deleteSection(Section section) {
        // 연관관계에 있는 Kudos 테이블의 row를 먼저 삭제한다.
        if (section.isKudosTemplate()) {
            kudosRepository.deleteBySectionId(section.getId());
        }
        // Section 삭제
        sectionRepository.delete(section);
    }

    private static void validateActionItems(Section section) {
        if (section.isNotActionItemsSection()) {
            throw new IllegalArgumentException("Action Items 유형만 사용자를 지정할 수 있습니다.");
        }
    }

    private void assignActionItem(User user, Team team, Section section,
        Retrospective retrospective) {
        actionItemRepository.save(createActionItem(user, team, section, retrospective));
    }

    private AssignKudosResponseDto convertAssignKudosResponse(KudosTarget kudosTarget) {
        return AssignKudosResponseDto.builder()
            .kudosId(kudosTarget.getId())
            .sectionId(kudosTarget.getSection().getId())
            .userId(kudosTarget.getUser().getId()).build();
    }

    private KudosTarget getKudosSection(Section section) {
        return kudosRepository.findBySectionId(section.getId());
    }

    private IncreaseSectionLikesResponseDto convertIncreaseSectionLikesResponseDto(
        Section section) {
        return new IncreaseSectionLikesResponseDto(section.getId(), section.getLikeCnt());
    }

    private Notification createNotification(Section section, Retrospective retrospective,
        User sender, User receiver, Likes likes) {
        return Notification.builder().section(section).retrospective(retrospective)
            .sender(sender).receiver(receiver).comment(null).likes(likes)
            .notificationType(NotificationType.LIKE).build();
    }

    private Likes createLikes(Section section, User user) {
        return Likes.builder().user(user).section(section).build();
    }

    private void deleteNotification(Likes likes) {
        notificationRepository.findNotificationByLikesId(likes.getId())
            .ifPresent(notificationRepository::delete);
    }

    private ActionItem getActionItem(Section section) {
        return actionItemRepository.findBySectionId(section.getId()).orElse(null);
    }

    private Retrospective findRetrospectiveById(Long retrospectiveId) {
        return retrospectiveRepository.findById(retrospectiveId)
            .orElseThrow(() -> new EntityNotFoundException("회고카드가 조회되지 않습니다."));
    }

    private TemplateSection findTemplateSectionById(Long templateSectionId) {
        return templateSectionRepository.findById(templateSectionId)
            .orElseThrow(() -> new EntityNotFoundException("회고카드를 작성하기 위한 템플릿이 조회되지 않습니다."));
    }

    private static void validationTemplateSection(Retrospective retrospective, TemplateSection templateSection) {
        retrospective.isTemplateSectionIncludedInRetrospectiveTemplate(templateSection);
    }

    private static void addCommentsToSection(List<GetSectionsResponseDto> sections,
        Map<Long, List<GetCommentDto>> sectionCommentsMap) {
        sections.forEach(section -> {
            List<GetCommentDto> sectionComments = sectionCommentsMap.get(section.getSectionId());
            section.addComments(sectionComments);
        });
    }

    private static Map<Long, List<GetCommentDto>> createGroupCommentsBySectionId(List<Comment> comments) {
        return comments.stream()
            .map(GetCommentDto::from)
            .collect(Collectors.groupingBy(GetCommentDto::getSectionId));
    }

    private List<Comment> getCommentsBy(List<Long> sectionIds) {
        return commentRepository.findAllBySectionIdIn(sectionIds);
    }

    private static List<Long> extractSectionIds(List<GetSectionsResponseDto> sections) {
        return sections.stream()
            .map(GetSectionsResponseDto::getSectionId)
            .collect(Collectors.toList());
    }

    private List<GetSectionsResponseDto> getSectionsWithoutComments(Long retrospectiveId) {
        return sectionRepository.getSections(retrospectiveId);
    }

}