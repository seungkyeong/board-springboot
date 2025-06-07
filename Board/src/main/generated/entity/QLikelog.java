package entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikelog is a Querydsl query type for Likelog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikelog extends EntityPathBase<Likelog> {

    private static final long serialVersionUID = 80294178L;

    public static final QLikelog likelog = new QLikelog("likelog");

    public final StringPath boardSysNo = createString("boardSysNo");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final StringPath sysNo = createString("sysNo");

    public final StringPath userId = createString("userId");

    public final StringPath userSysNo = createString("userSysNo");

    public QLikelog(String variable) {
        super(Likelog.class, forVariable(variable));
    }

    public QLikelog(Path<? extends Likelog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikelog(PathMetadata metadata) {
        super(Likelog.class, metadata);
    }

}

