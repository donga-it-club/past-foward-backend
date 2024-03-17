package aws.retrospective.service;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.dto.IncreaseSectionLikesRequestDto;
import aws.retrospective.dto.IncreaseSectionLikesResponseDto;
import aws.retrospective.entity.Likes;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.exception.ErrorCode;
import aws.retrospective.exception.custom.MismatchedDataException;
import aws.retrospective.repository.LikesRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
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

    // Section 등록
    @Transactional
    public CreateSectionResponseDto createSection(CreateSectionDto request) {

        User findUser = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new NoSuchElementException("사용자가 조회되지 않습니다."));
        Retrospective findRetrospective = retrospectiveRepository.findById(
                request.getRetrospectiveId())
            .orElseThrow(() -> new NoSuchElementException("회고보드가 조회되지 않습니다"));
        TemplateSection findTemplateSection = templateSectionRepository.findById(
                request.getTemplateSectionId())
            .orElseThrow(() -> new NoSuchElementException("템플릿 섹션이 조회되지 않습니다."));

        // 회고보드와 템플릿 섹션에 등록된 회고보드 유형 이름이 다르면 예외를 발생한다.
        if (!findRetrospective.getTemplate().getName()
            .equals(findTemplateSection.getTemplate().getName())) {
            throw new IllegalArgumentException("템플릿 정보가 일치하지 않습니다.");
        }

        // 섹션 등록
        Section createSection = createSection(request.getSectionContent(), findTemplateSection,
            findRetrospective, findUser);
        sectionRepository.save(createSection);

        return new CreateSectionResponseDto(createSection.getId(),
            createSection.getUser().getId(), request.getRetrospectiveId(), request.getSectionContent());
    }

    // 섹션 수정
    @Transactional
    public EditSectionResponseDto updateSectionContent(Long sectionId, EditSectionRequestDto request) {
        Section findSection = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("섹션이 조회되지 않습니다."));
        User loginedUser = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new NoSuchElementException("사용자가 조회되지 않습니다."));

        // 섹션 수정은 해당 섹션 작성자만 가능하다.
        if (!findSection.getUser().equals(loginedUser)) {
            throw new IllegalStateException("섹션 수정은 해당 섹션 작성자만 가능합니다.");
        }

        // 섹션 수정
        findSection.updateSection(request.getSectionContent());

        return new EditSectionResponseDto(sectionId, request.getSectionContent());
    }

    // 섹션 좋아요 API
    @Transactional
    public IncreaseSectionLikesResponseDto increaseSectionLikes(Long sectionId, IncreaseSectionLikesRequestDto request) {
        // 섹션 조회
        Section findSection = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("Section이 조회되지 않습니다."));
        User findUser = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new NoSuchElementException("사용자가 조회되지 않습니다."));

        // 해당 Section을 사용자가 작성한 것이 아니라면 예외가 발생한다.
        if(!findSection.getUser().equals(findUser)) {
            throw new MismatchedDataException(ErrorCode.MISMATCHED_DATA_EXCEPTION);
        }

        // 사용자가 해당 Section에 좋아요를 눌렀는지 확인한다.
        Optional<Likes> findLikes = likesRepository.findByUserAndSection(findUser,
            findSection);
        // 좋아요를 누른적이 없을 때는 좋아요 횟수를 증가시킨다.
        if(findLikes.isEmpty()) {
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

    // 섹션 등록
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
}
