package entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -370992901L;

    public static final QBoard board = new QBoard("board");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath imgPath = createString("imgPath");

    public final NumberPath<Long> like = createNumber("like", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final StringPath sysNo = createString("sysNo");

    public final StringPath title = createString("title");

    public final StringPath userId = createString("userId");

    public final StringPath userSysNo = createString("userSysNo");

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

