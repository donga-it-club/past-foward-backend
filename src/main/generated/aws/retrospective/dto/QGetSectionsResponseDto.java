package aws.retrospective.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * aws.retrospective.dto.QGetSectionsResponseDto is a Querydsl Projection type for GetSectionsResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QGetSectionsResponseDto extends ConstructorExpression<GetSectionsResponseDto> {

    private static final long serialVersionUID = 1614758475L;

    public QGetSectionsResponseDto(com.querydsl.core.types.Expression<Long> sectionId, com.querydsl.core.types.Expression<String> username, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Long> likeCnt, com.querydsl.core.types.Expression<String> sectionName, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdDate) {
        super(GetSectionsResponseDto.class, new Class<?>[]{long.class, String.class, String.class, long.class, String.class, java.time.LocalDateTime.class}, sectionId, username, content, likeCnt, sectionName, createdDate);
    }

}

