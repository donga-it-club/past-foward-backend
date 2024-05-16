package aws.retrospective.service;

import aws.retrospective.dto.AssignKudosRequestDto;
import aws.retrospective.dto.AssignKudosResponseDto;
import aws.retrospective.dto.AssignUserRequestDto;
import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.GetActionItemsResponseDto;
import aws.retrospective.dto.GetCommentDto;
import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.dto.SectionNotificationDto;
import aws.retrospective.entity.ActionItem;
import aws.retrospective.entity.Comment;
import aws.retrospective.entity.KudosTarget;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.repository.ActionItemRepository;
import aws.retrospective.repository.CommentRepository;
import aws.retrospective.repository.KudosTargetRepository;
import aws.retrospective.repository.LikesRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final CommentRepository commentRepository;
    private final StringRedisTemplate redisTemplate;
    private final KudosTargetRepository kudosRepository;

    private final String SECTION_ID = "sectionId_";

    // 회고 카드 전체 조회
    @Transactional(readOnly = true)
    public List<GetSectionsResponseDto> getSections(GetSectionsRequestDto request) {
        Retrospective findRetrospective = getRetrospective(request.getRetrospectiveId());

        // 개인 회고 조회 시에 teamId 필요 X
        if (findRetrospective.getTeam() == null) {
            if (request.getTeamId() != null) {
                throw new IllegalArgumentException("개인 회고 조회 시 팀 정보는 필요하지 않습니다.");
            }
        }

        // 다른 팀의 회고 보드를 조회 할 수 없다
        if (request.getTeamId() != null) {
            Team findTeam = getTeam(request.getTeamId());
            if (!findRetrospective.isSameTeam(findTeam)) {
                throw new ForbiddenAccessException("다른 팀의 회고보드에 접근할 수 없습니다.");
            }
        }

        List<GetSectionsResponseDto> response = new ArrayList<>();
        List<Section> sections = sectionRepository.getSectionsWithComments(
            request.getRetrospectiveId());
        revertDto(sections, response);

        return response;
    }

    // 회고 카드 등록
    @Transactional
    public CreateSectionResponseDto createSection(User user, CreateSectionDto request) {
        Retrospective findRetrospective = getRetrospective(request.getRetrospectiveId());
        TemplateSection findTemplateSection = getTemplateSection(request.getTemplateSectionId());

        // 회고 템플릿 정보가 일치하는지 확인한다.
        if (!findRetrospective.isSameTemplate(findTemplateSection)) {
            throw new IllegalArgumentException("회고 템플릿 정보가 일치하지 않습니다.");
        }

        // 회고 카드 등록
        Section createSection = createSection(request.getSectionContent(), findTemplateSection,
            findRetrospective, user);
        sectionRepository.save(createSection);

        return new CreateSectionResponseDto(createSection.getId(), createSection.getUser().getId(),
            request.getRetrospectiveId(), request.getSectionContent());
    }

    // 회고 카드 수정
    @Transactional
    public EditSectionResponseDto updateSectionContent(User user, Long sectionId,
        EditSectionRequestDto request) {
        Section findSection = getSection(sectionId);

        // 회고 카드 수정은 작성자만 가능하다.
        if (!findSection.isSameUser(user)) {
            throw new ForbiddenAccessException("회고 카드를 수정할 권한이 없습니다.");
        }

        // 회고 카드 수정
        findSection.updateSection(request.getSectionContent());

        return new EditSectionResponseDto(sectionId, request.getSectionContent());
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
            Likes createLikes = Likes.builder().section(findSection).user(user).build();
            likesRepository.save(createLikes);
            findSection.increaseSectionLikes();
        } else {
            Likes likes = findLikes.get();
            likesRepository.delete(likes);
            findSection.cancelSectionLikes();
        }

        return new IncreaseSectionLikesResponseDto(findSection.getId(), findSection.getLikeCnt());
    }

    // Action Items 사용자 지정
    @Transactional
    public void assignUserToActionItem(AssignUserRequestDto request) {
        Team team = getTeam(request.getTeamId());
        Retrospective retrospective = getRetrospective(request.getRetrospectiveId());
        Section section = getSection(request.getSectionId());

        if (!section.isActionItemsSection()) {
            throw new IllegalArgumentException("Action Items 유형만 사용자를 지정할 수 있습니다.");
        }

        ActionItem actionItem = actionItemRepository.findBySectionId(section.getId()).orElse(null);
        User assignUser = getAssignUser(request);

        if (actionItem == null) {
            ActionItem savedActionItem = actionItemRepository.save(
                createActionItem(assignUser, team, section, retrospective));
            section.updateActionItems(savedActionItem);

        } else {
            actionItem.assignUser(assignUser);
        }
    }

    // 회고카드 삭제
    @Transactional
    public void deleteSection(Long sectionId, User user) {
        Section findSection = getSection(sectionId);

        // 작성자만 회고 카드를 삭제할 수 있다.
        if (!findSection.isSameUser(user)) {
            throw new ForbiddenAccessException("작성자만 회고 카드를 삭제할 수 있습니다.");
        }

        // 연관관계에 있는 Kudos 테이블의 row를 먼저 삭제한다.
        if (findSection.isKudosTemplate()) {
            kudosRepository.deleteBySectionId(sectionId);
        }

        sectionRepository.delete(findSection);
    }

    @Transactional
    public List<SectionNotificationDto> getNewCommentsAndLikes() {
        // 1. 모든 Section을 조회한다.
        List<Section> sections = sectionRepository.findAll();
        List<SectionNotificationDto> notifications = new ArrayList<>();

        // 2. 모든 Section 순회한다.
        sections
            .forEach(section -> {
                // DB에서 마지막으로 작성된 댓글과 좋아요를 조회한다.
                Comment lastCommentInDB = getLatestCommentBySection(section);
                Likes lastLikeInDB = getLatestLikeBySection(section);
                LocalDateTime lastCommentTimeInDB = getLastTimeInDB(lastCommentInDB);
                LocalDateTime lastLikeTimeInDB = getLastTimeInDB(lastLikeInDB);

                // Redis에서 마지막으로 알림이 전송된 시간을 조회한다.
                String lastCommentTimeInRedis = getLastTimeInRedis(SECTION_ID + section.getId(),
                    "comment");
                String lastLikeTimeInRedis = getLastTimeInRedis(SECTION_ID + section.getId(),
                    "like");

                // 마지막으로 작성된 댓글과 좋아요 시간을 Redis에 저장한다.
                updateRedisValue(section, lastCommentTimeInDB, lastLikeTimeInDB);

                // 마지막으로 작성된 댓글과 좋아요 시간을 비교하여 새로운 댓글과 좋아요를 조회한다.
                List<Comment> comments = getComments(section.getId(), lastCommentTimeInRedis);
                List<Likes> likes = getLikes(section.getId(), lastLikeTimeInRedis);

                // 새로운 댓글과 좋아요가 있을 경우 알림을 생성한다.
                if (!likes.isEmpty() || !comments.isEmpty()) {
                    notifications.add(createNotification(section, comments, likes));
                }
            });

        return notifications;
    }

    private void updateRedisValue(Section section, LocalDateTime lastCommentTimeInDB,
        LocalDateTime lastLikeTimeInDB) {
        saveLastTimeInRedis(SECTION_ID + section.getId(), "comment", lastCommentTimeInDB);
        saveLastTimeInRedis(SECTION_ID + section.getId(), "like", lastLikeTimeInDB);
    }

    private LocalDateTime getLastTimeInDB(Object entity) {
        if (entity != null) {
            if (entity instanceof Comment) {
                return ((Comment) entity).getCreatedDate();
            } else if (entity instanceof Likes) {
                return ((Likes) entity).getCreatedDate();
            }
        }
        return null;
    }

    private String getLastTimeInRedis(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    private void saveLastTimeInRedis(String key, String hashKey, LocalDateTime lastTimeInDB) {
        if (lastTimeInDB != null) {
            redisTemplate.opsForHash().put(key, hashKey, lastTimeInDB.toString());
        }
    }

    private Likes getLatestLikeBySection(Section section) {
        return likesRepository.findLatestLikeBySection(section.getId());
    }

    private Comment getLatestCommentBySection(Section section) {
        return commentRepository.findLatestCommentBySection(section.getId());
    }

    private List<Comment> getComments(Long sectionId, String lastCommentTimeInRedis) {
        if (lastCommentTimeInRedis != null) {
            return commentRepository.findCommentsAfterDate(sectionId,
                convertStringToLocalDateTime(lastCommentTimeInRedis));
        } else {
            return commentRepository.findAllComments(sectionId);
        }
    }

    private List<Likes> getLikes(Long sectionId, String lastLikeTimeInRedis) {
        if (lastLikeTimeInRedis != null) {
            return likesRepository.findLikesAfterDate(sectionId,
                convertStringToLocalDateTime(lastLikeTimeInRedis));
        } else {
            return likesRepository.findAllLikes(sectionId);
        }
    }

    private SectionNotificationDto createNotification(Section section, List<Comment> comments,
        List<Likes> likes) {
        return SectionNotificationDto.createNotification(section, comments, likes);
    }

    @Transactional
    public AssignKudosResponseDto assignKudos(Long sectionId, AssignKudosRequestDto request) {
        Section section = getSection(sectionId); // Kudos Section

        // Kudos 유형에만 칭창할 사람을 지정8할 수 있다.
        validateKudosTemplate(sectionId, section);

        User targetUser = getUser(request); // 칭찬 대상 조회
        /**
         * 이전에 해당 Section에 다른 사람을 칭창할 사람으로 지정한 적이 있는지 조회
         * 조회O : DB 값만 변경하면 된다.
         * 조회X : DB에 새로운 row 삽입
         */
        KudosTarget kudosSection = kudosRepository.findBySectionId(section.getId());

        if (kudosSection == null) {
            return AssignKudosResponseDto.convertResponse(assignKudos(section, targetUser));
        } else {
            kudosSection.assignUser(targetUser);
            return AssignKudosResponseDto.convertResponse(kudosSection);
        }
    }

    private static void validateKudosTemplate(Long sectionId, Section section) {
        if (section.isNotKudosTemplate()) {
            throw new IllegalArgumentException(
                "Kudos Section이 아닙니다. id : " + sectionId);
        }
    }

    private TemplateSection getTemplateSection(Long sectionId) {
        return templateSectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("Section이 조회되지 않습니다."));
    }

    private Retrospective getRetrospective(Long retrospectiveId) {
        return retrospectiveRepository.findById(retrospectiveId)
            .orElseThrow(() -> new NoSuchElementException("회고보드가 조회되지 않습니다."));
    }

    private Section getSection(Long sectionId) {
        return sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("회고 카드가 조회되지 않습니다."));
    }

    // 회고 카드 등록
    private Section createSection(String sectionContent, TemplateSection findTemplateSection,
        Retrospective findRetrospective, User findUser) {
        return Section.builder().templateSection(findTemplateSection)
            .retrospective(findRetrospective).user(findUser).likeCnt(0).content(sectionContent)
            .build();
    }

    private void revertDto(List<Section> sections, List<GetSectionsResponseDto> response) {
        for (Section section : sections) {
            List<GetCommentDto> collect = section.getComments().stream()
                .map(c -> new GetCommentDto(c.getId(), c.getUser().getId(), c.getContent(),
                    c.getUser().getUsername(),
                    c.getUser().getThumbnail()))
                .collect(Collectors.toList());
            response.add(
                new GetSectionsResponseDto(section.getId(), section.getUser().getId(),
                    section.getUser().getUsername(),
                    section.getContent(), section.getLikeCnt(),
                    section.getTemplateSection().getSectionName(), section.getCreatedDate(),
                    collect, section.getUser().getThumbnail(),
                    section.getActionItem() != null ? new GetActionItemsResponseDto(
                        section.getActionItem().getUser().getId(),
                        section.getActionItem().getUser().getUsername(),
                        section.getActionItem().getUser().getThumbnail()
                    ) : null));
        }
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new NoSuchElementException("Not Found Team id : " + teamId));
    }

    private static ActionItem createActionItem(User findUser, Team findTeam, Section section,
        Retrospective retrospective) {
        return ActionItem.builder().user(findUser).team(findTeam).section(section)
            .retrospective(retrospective).build();
    }

    private User getAssignUser(AssignUserRequestDto request) {
        return userRepository.findById(request.getUserId()).orElseThrow(
            () -> new NoSuchElementException("Not Found User Id : " + request.getUserId()));
    }

    private static LocalDateTime convertStringToLocalDateTime(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
  
    private User getUser(AssignKudosRequestDto request) {
        return userRepository.findById(request.getUserId())
            .orElseThrow(
                () -> new NoSuchElementException("Not Found User Id : " + request.getUserId()));
    }

    private KudosTarget assignKudos(Section section, User targetUser) {
        return kudosRepository.save(KudosTarget.createKudosTarget(section, targetUser));
    }
}