package aws.retrospective.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRetrospective is a Querydsl query type for Retrospective
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRetrospective extends EntityPathBase<Retrospective> {

    private static final long serialVersionUID = -1748903180L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRetrospective retrospective = new QRetrospective("retrospective");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final ListPath<Bookmark, QBookmark> bookmarks = this.<Bookmark, QBookmark>createList("bookmarks", Bookmark.class, QBookmark.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> deletedDate = createDateTime("deletedDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final EnumPath<ProjectStatus> status = createEnum("status", ProjectStatus.class);

    public final QTeam team;

    public final QRetrospectiveTemplate template;

    public final ComparablePath<java.util.UUID> thumbnail = createComparable("thumbnail", java.util.UUID.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public final QUser user;

    public QRetrospective(String variable) {
        this(Retrospective.class, forVariable(variable), INITS);
    }

    public QRetrospective(Path<? extends Retrospective> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRetrospective(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRetrospective(PathMetadata metadata, PathInits inits) {
        this(Retrospective.class, metadata, inits);
    }

    public QRetrospective(Class<? extends Retrospective> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new QTeam(forProperty("team")) : null;
        this.template = inits.isInitialized("template") ? new QRetrospectiveTemplate(forProperty("template")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

