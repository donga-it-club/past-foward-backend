package aws.retrospective.repository;

import static aws.retrospective.entity.QRetrospective.retrospective;
import static aws.retrospective.entity.QSection.section;
import static aws.retrospective.entity.QTemplateSection.templateSection;
import static aws.retrospective.entity.QUser.user;

import aws.retrospective.dto.GetSectionsResponseDto;
import aws.retrospective.dto.QGetSectionsResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SectionRepositoryCustomImpl implements SectionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetSectionsResponseDto> getSections(Long retrospectiveId) {
        return queryFactory.
            select(
                new QGetSectionsResponseDto(section.id, user.username, section.content,
                    section.likeCnt, templateSection.templateStatus, section.createdDate))
            .from(section)
            .join(section.retrospective, retrospective)
            .join(section.user, user)
            .join(section.templateSection, templateSection)
            .where(retrospective.id.eq(retrospectiveId))
            .fetch();
    }

}
