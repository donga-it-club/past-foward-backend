package aws.retrospective.service;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
import aws.retrospective.dto.EditSectionRequestDto;
import aws.retrospective.dto.EditSectionResponseDto;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import java.util.NoSuchElementException;
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

    @Transactional
    public void deleteSection(Long sectionId) {
        Section findSection = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new NoSuchElementException("section이 조회되지 않습니다."));
        sectionRepository.delete(findSection);
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
