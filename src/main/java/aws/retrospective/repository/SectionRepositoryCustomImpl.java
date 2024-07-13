package aws.retrospective.repository;

import static aws.retrospective.entity.QActionItem.actionItem;
import static aws.retrospective.entity.QComment.comment;
import static aws.retrospective.entity.QKudosTarget.kudosTarget;
import static aws.retrospective.entity.QSection.section;
import static aws.retrospective.entity.QUser.user;

import aws.retrospective.dto.GetCommentDto;
import aws.retrospective.dto.GetSectionsResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SectionRepositoryCustomImpl implements SectionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetSectionsResponseDto> getSectionsAll(Long retrospectiveId) {

        // 댓글을 제외한 ActionItem, Kudos, Section 조회
        List<GetSectionsResponseDto> result = getSections(retrospectiveId);

        // result에 포함된 모든 Section의 ID 조회
        List<Long> sectionIds = getSectionIds(result);

        // Section ID와 일치하는 댓글 조회
        List<GetCommentDto> comments = getComments(sectionIds);

        // comments에 등록된 댓글을 Map으로 변환 key:value=comment.id:comment
        // 아래에서 루프를 돌 때, IN 절을 사용하기 위해 id로 매핑
        Map<Long, List<GetCommentDto>> collect = createCommentMap(comments);

        // result에는 댓글 정보가 포함되어 있지 않기 때문에 루프를 돌면서 컬렉션 추가
        result.forEach(
            section -> section.addComments(collect.get(section.getSectionId()))
        );

        return result;
    }

    private static Map<Long, List<GetCommentDto>> createCommentMap(List<GetCommentDto> comments) {
        return comments.stream()
            .collect(Collectors.groupingBy(GetCommentDto::getSectionId));
    }

    private List<GetCommentDto> getComments(List<Long> sectionIds) {
        return queryFactory
            .select(Projections.constructor(GetCommentDto.class,
                comment.section.id, comment.id, comment.user.id, comment.content, comment.user.username,
                comment.user.thumbnail, comment.createdDate, comment.updatedDate))
            .from(comment)
            .join(comment.user, user)
            .where(comment.id.in(sectionIds))
            .fetch();
    }

    private static List<Long> getSectionIds(List<GetSectionsResponseDto> result) {
        return result.stream()
            .map(s -> s.getSectionId())
            .toList();
    }

    private List<GetSectionsResponseDto> getSections(Long retrospectiveId) {
        return queryFactory
            .select(Projections.constructor(GetSectionsResponseDto.class,
                section.id, section.user.id, section.user.username, section.content,
                section.likeCnt, section.templateSection.sectionName, section.createdDate,
                section.user.thumbnail, actionItem, kudosTarget
            ))
            .from(section)
            .leftJoin(actionItem).on(actionItem.section.eq(section))
            .leftJoin(kudosTarget).on(kudosTarget.section.eq(section))
            .leftJoin(section.comments, comment)
            .where(section.retrospective.id.eq(retrospectiveId))
            .fetch();
    }
}