package aws.retrospective.repository;

import static aws.retrospective.entity.QActionItem.actionItem;
import static aws.retrospective.entity.QKudosTarget.kudosTarget;
import static aws.retrospective.entity.QSection.section;
import static aws.retrospective.entity.QTemplateSection.templateSection;
import static aws.retrospective.entity.QUser.user;

import aws.retrospective.dto.GetSectionsResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SectionRepositoryCustomImpl implements SectionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetSectionsResponseDto> getSections(Long retrospectiveId) {
        return queryFactory
            .select(Projections.constructor(
                GetSectionsResponseDto.class,
                section.id, user.id, user.username, section.content,
                section.likeCnt, templateSection.sectionName, section.createdDate,
                user.thumbnail, actionItem, kudosTarget
            ))
            .from(section)
            .join(section.user, user)
            .leftJoin(actionItem).on(actionItem.section.id.eq(section.id)).fetchJoin()
            .leftJoin(kudosTarget).on(kudosTarget.section.id.eq(section.id)).fetchJoin()
            .where(section.retrospective.id.eq(retrospectiveId))
            .orderBy(section.createdDate.desc())
            .fetch();
    }

}