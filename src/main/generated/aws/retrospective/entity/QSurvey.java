package aws.retrospective.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSurvey is a Querydsl query type for Survey
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurvey extends EntityPathBase<Survey> {

    private static final long serialVersionUID = 316679939L;

    public static final QSurvey survey = new QSurvey("survey");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath age = createString("age");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final EnumPath<Survey.Gender> gender = createEnum("gender", Survey.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath occupation = createString("occupation");

    public final StringPath purpose = createString("purpose");

    public final StringPath region = createString("region");

    public final StringPath source = createString("source");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QSurvey(String variable) {
        super(Survey.class, forVariable(variable));
    }

    public QSurvey(Path<? extends Survey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSurvey(PathMetadata metadata) {
        super(Survey.class, metadata);
    }

}

