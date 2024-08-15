package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;

import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Section;
import aws.retrospective.entity.TemplateSection;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.TemplateSectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.util.TestUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SectionServiceIntegrationTest {

    @Autowired
    SectionService sectionService;
    @Autowired
    RetrospectiveRepository retrospectiveRepository;
    @Autowired
    RetrospectiveTemplateRepository retrospectiveTemplateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TemplateSectionRepository templateSectionRepository;
    @Autowired
    private SectionRepository sectionRepository;

    private User user;
    private RetrospectiveTemplate template;
    private Retrospective retrospective;
    private TemplateSection templateSection;
    private static final int SECTION_CNT = 10;

    @BeforeEach
    void setUp() {
        // given
        user = TestUtil.createUser();
        template = TestUtil.createTemplate();
        retrospective = TestUtil.createRetrospective(template, user,
            null);
        templateSection = TemplateSection.builder().sectionName("Kudos")
            .template(template).sequence(1).build();

        templateSectionRepository.save(templateSection);
        userRepository.save(user);
        retrospectiveRepository.save(retrospective);
        retrospectiveTemplateRepository.save(template);
    }

    @Test
    @DisplayName("정렬 조건 1순위로 좋아요 개수가 많은 순으로 섹션을 조회한다.")
    void getSectionsByLikeCntPriority() {
        // given
        for (int i = 1; i <= SECTION_CNT; i++) {
            Section section = Section.builder()
                .templateSection(templateSection)
                .user(user)
                .content("test" + i)
                .retrospective(retrospective)
                .build();
            increaseLikeCnt(section, i);
            sectionRepository.save(section);
        }

        // when
        GetSectionsRequestDto request = new GetSectionsRequestDto();
        request.setRetrospectiveId(retrospective.getId());
        List<GetSectionsResponseDto> result = sectionService.getSections(request);

        // then
        assertThat(result.size()).isEqualTo(SECTION_CNT);
        for (int i = 0; i < SECTION_CNT; i++) {
            assertThat(result.get(i).getLikeCnt()).isEqualTo(SECTION_CNT - i);
        }
    }


    @Test
    @DisplayName("정렬 조건 2순위로 작성일자가 최신 순으로 섹션을 조회한다.")
    void getSectionsByCreatedDatePriority() {
        // given
        for(int i = 1; i <= SECTION_CNT; i++) {
            Section section = Section.builder()
                .templateSection(templateSection)
                .user(user)
                .content(i + "")
                .retrospective(retrospective)
                .build();
            sectionRepository.save(section);
        }

        // when
        GetSectionsRequestDto request = new GetSectionsRequestDto();
        request.setRetrospectiveId(retrospective.getId());
        List<GetSectionsResponseDto> result = sectionService.getSections(request);

        // then
        assertThat(result.size()).isEqualTo(SECTION_CNT);
        for (int i = 0; i < SECTION_CNT; i++) {
            assertThat(result.get(i).getContent()).isEqualTo((SECTION_CNT - i) + "");
        }
    }

    @Test
    @DisplayName("좋아요 개수가 동일한 경우 작성일자가 최신 순으로 섹션을 조회한다.")
    void getSections_WhenLikeCntIsSame_ThenSortByCreatedDateDesc() {
        // given
        for(int i = 1; i <= SECTION_CNT; i++) {
            Section section = Section.builder()
                .templateSection(templateSection)
                .user(user)
                .content(i + "")
                .retrospective(retrospective)
                .build();
            if(i % 2 == 0) {
                increaseLikeCnt(section, 1);  // 짝수 번째의 likeCnt를 1개 증가
            }
            sectionRepository.save(section);
        }

        // when
        GetSectionsRequestDto request = new GetSectionsRequestDto();
        request.setRetrospectiveId(retrospective.getId());
        List<GetSectionsResponseDto> result = sectionService.getSections(request);

        // then
        assertThat(result.size()).isEqualTo(SECTION_CNT);

        // 좋아요 개수가 같은 경우 작성일자가 최신 순으로 정렬되었는지 검증
        for (int i = 1; i < result.size(); i++) {
            GetSectionsResponseDto current = result.get(i);
            GetSectionsResponseDto previous = result.get(i - 1);

            // 좋아요 개수가 동일한 경우 작성일자가 최신 순인지 확인
            if (current.getLikeCnt() == (previous.getLikeCnt())) {
                assertThat(current.getCreatedDate())
                    .isBeforeOrEqualTo(previous.getCreatedDate());
            }
        }
    }

    private void increaseLikeCnt(Section section, int cnt) {
        for (int i = 0; i < cnt; i++) {
            section.increaseSectionLikes();
        }
    }

}
