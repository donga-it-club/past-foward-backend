package aws.retrospective.service;

import aws.retrospective.dto.CreateSectionDto;
import aws.retrospective.dto.CreateSectionResponseDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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

        TemplateSection createTemplateSection = createTemplateSection(request, findRetrospective);
        templateSectionRepository.save(createTemplateSection);

        Section createSection = createSection(request, findRetrospective, findUser,
            createTemplateSection);
        sectionRepository.save(createSection);

        return new CreateSectionResponseDto(createSection.getUser().getId(),
            createSection.getRetrospective().getId(),
            createSection.getTemplateSection().getSectionName(), createSection.getContent(), createTemplateSection.getSequence());
    }

    // 섹션 생성
    private static Section createSection(CreateSectionDto request, Retrospective findRetrospective,
        User findUser, TemplateSection createTemplateSection) {
        return Section.builder()
            .content(request.getSectionContent())
            .likeCnt(0)
            .retrospective(findRetrospective)
            .user(findUser)
            .templateSection(createTemplateSection)
            .build();
    }

    // 섹션 템플릿 생성
    private TemplateSection createTemplateSection(CreateSectionDto request,
        Retrospective findRetrospective) {
        return TemplateSection.builder()
            .sectionName(request.getSectionName())
            .template(findRetrospective.getTemplate())
            .sequence(findSectionSequence(request.getSectionName()) + 1) // 현재 섹션에 등록된 개수 + 1
            .build();
    }

    // 섹션에 등록된 게시물 개수 반환
    private int findSectionSequence(String sectionName) {
        return sectionRepository.countByTemplateSectionSectionName(sectionName).intValue();
    }

}
