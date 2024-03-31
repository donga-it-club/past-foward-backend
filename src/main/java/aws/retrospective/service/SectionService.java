package aws.retrospective.service;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.DeleteSectionRequestDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.FindSectionCountRequestDto;
import aws.retrospective.dto.FindSectionCountResponseDto;
import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.dto.GetTeamUsersRequestDto;
import aws.retrospective.dto.GetTeamUsersResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesRequestDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.exception.custom.ForbiddenAccessException;
import aws.retrospective.repository.LikesRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.repository.UserTeamRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final RetrospectiveRepository retrospectiveRepository;
    private final TemplateSectionRepository templateSectionRepository;
    private final LikesRepository likesRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;

    // 회고 카드 등록
    @Transactional
    public CreateSectionResponseDto createSection(CreateSectionDto request) {

        User findUser = getUser(request.getUserId());
        Retrospective findRetrospective = retrospectiveRepository.findById(
                request.getRetrospectiveId())
            .orElseThrow(() -> new NoSuchElementException("회고보드가 조회되지 않습니다"));
        TemplateSection findTemplateSection = templateSectionRepository.findById(
                request.getTemplateSectionId())
            .orElseThrow(() -> new NoSuchElementException("섹션이 조회되지 않습니다."));

        // 회고 템플릿 정보가 일치하는지 확인한다.
        if (!findRetrospective.getTemplate().getName()
            .equals(findTemplateSection.getTemplate().getName())) {
            throw new IllegalArgumentException("회고 템플릿 정보가 일치하지 않습니다.");
        }

        // 회고 카드 등록
        Section createSection = createSection(request.getSectionContent(), findTemplateSection,
            findRetrospective, findUser);
        sectionRepository.save(createSection);

        return new CreateSectionResponseDto(createSection.getId(),
            createSection.getUser().getId(), request.getRetrospectiveId(),
            request.getSectionContent());
    }

    // 회고 카드 수정
    @Transactional
    public EditSectionResponseDto updateSectionContent(Long sectionId,
        EditSectionRequestDto request) {
        Section findSection = getSection(sectionId);
        User loginedUser = getUser(request.getUserId());

        // 회고 카드 수정은 작성자만 가능하다.
        if (!findSection.getUser().equals(loginedUser)) {
            throw new ForbiddenAccessException("회고 카드를 수정할 권한이 없습니다.");
        }

        // 회고 카드 수정
        findSection.updateSection(request.getSectionContent());

        return new EditSectionResponseDto(sectionId, request.getSectionContent());
    }

    // 회고 카드 좋아요 API
    @Transactional
    public IncreaseSectionLikesResponseDto increaseSectionLikes(Long sectionId,
        IncreaseSectionLikesRequestDto request) {
        // 회고 카드 조회
        Section findSection = getSection(sectionId);
        User findUser = getUser(request.getUserId());

        // 사용자가 해당 회고 카드에 좋아요를 눌렀는지 확인한다.
        Optional<Likes> findLikes = likesRepository.findByUserAndSection(findUser,
            findSection);
        // 좋아요를 누른적이 없을 때는 좋아요 횟수를 증가시킨다.
        if (findLikes.isEmpty()) {
            Likes createLikes = Likes.builder()
                .section(findSection)
                .user(findUser)
                .build();
            likesRepository.save(createLikes);
            findSection.increaseSectionLikes();
        } else {
            Likes likes = findLikes.get();
            likesRepository.delete(likes);
            findSection.cancelSectionLikes();
        }

        return new IncreaseSectionLikesResponseDto(findSection.getId(), findSection.getLikeCnt());
    }

    // 회고 카드 개수 조회
    @Transactional(readOnly = true)
    public FindSectionCountResponseDto getSectionCounts(FindSectionCountRequestDto request) {
        Retrospective retrospective = getRetrospective(request.getRetrospectiveId());
        TemplateSection templateSection = getTemplateSection(request.getTemplateSectionId());

        int sectionCounts = sectionRepository.countByRetrospectiveAndTemplateSection(retrospective,
            templateSection);

        return new FindSectionCountResponseDto(sectionCounts);
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

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자가 조회되지 않습니다."));
    }

    @Transactional
    public void deleteSection(Long sectionId, DeleteSectionRequestDto request) {
        Section findSection = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("회고 카드가 조회되지 않습니다."));
        User findUser = getUser(request.getUserId());

        // 작성자만 회고 카드를 삭제할 수 있다.
        if(!findSection.getUser().equals(findUser)) {
            throw new ForbiddenAccessException("작성자만 회고 카드를 삭제할 수 있습니다.");
        }

        sectionRepository.delete(findSection);
    }

    // 회고 카드 등록
    private Section createSection(String sectionContent, TemplateSection findTemplateSection,
        Retrospective findRetrospective, User findUser) {
        return Section.builder()
            .templateSection(findTemplateSection)
            .retrospective(findRetrospective)
            .user(findUser)
            .likeCnt(0)
            .content(sectionContent)
            .build();
    }

    // 회고 카드 전체 조회
    @Transactional(readOnly = true)
    public List<GetSectionsResponseDto> getSections(GetSectionsRequestDto request) {
        Retrospective findRetrospective = getRetrospective(request);
        Team findTeam = getTeam(request.getTeamId());

        // 다른 팀이 작성한 회고보드는 조회할 수 없다.
        if(findRetrospective.getTeam().getId() != findTeam.getId()) {
            throw new ForbiddenAccessException("해당 팀의 회고보드만 조회할 수 있습니다.");
        }

        return sectionRepository.getSections(request.getRetrospectiveId());
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(
                () -> new NoSuchElementException("Not Found Team id : " + teamId));
    }

    private Retrospective getRetrospective(GetSectionsRequestDto request) {
        return retrospectiveRepository.findById(request.getRetrospectiveId())
            .orElseThrow(() -> new NoSuchElementException(
                "Not Found Retrospective id : " + request.getRetrospectiveId()));
    }

    @Transactional(readOnly = true)
    public List<GetTeamUsersResponseDto> getTeamMembers(Long teamId, GetTeamUsersRequestDto request) {
        Team findTeam = getTeam(teamId);
        Retrospective findRetrospective = getRetrospective(request.getRetrospectiveId());

        // 다른 팀의 회고보드에 대한 조회는 불가능하다.
        if(findRetrospective.getTeam().getId() != findTeam.getId()) {
            throw new ForbiddenAccessException("해당 팀의 회고보드만 조회할 수 있습니다.");
        }

        return userTeamRepository.findTeamMembers(teamId);
    }
}
