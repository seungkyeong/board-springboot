package entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1235528470L;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath sysNo = createString("sysNo");

    public final StringPath userId = createString("userId");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

