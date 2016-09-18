package com.coreoz.plume.db.querydsl.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QSchemaVersion is a Querydsl query type for SchemaVersion
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSchemaVersion extends com.querydsl.sql.RelationalPathBase<SchemaVersion> {

    private static final long serialVersionUID = -859840069;

    public static final QSchemaVersion schemaVersion = new QSchemaVersion("schema_version");

    public final NumberPath<Integer> checksum = createNumber("checksum", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> executionTime = createNumber("executionTime", Integer.class);

    public final StringPath installedBy = createString("installedBy");

    public final DateTimePath<java.time.LocalDateTime> installedOn = createDateTime("installedOn", java.time.LocalDateTime.class);

    public final NumberPath<Integer> installedRank = createNumber("installedRank", Integer.class);

    public final StringPath script = createString("script");

    public final BooleanPath success = createBoolean("success");

    public final StringPath type = createString("type");

    public final StringPath version = createString("version");

    public final com.querydsl.sql.PrimaryKey<SchemaVersion> schemaVersionPk = createPrimaryKey(installedRank);

    public QSchemaVersion(String variable) {
        super(SchemaVersion.class, forVariable(variable), "PUBLIC", "schema_version");
        addMetadata();
    }

    public QSchemaVersion(String variable, String schema, String table) {
        super(SchemaVersion.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSchemaVersion(Path<? extends SchemaVersion> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "schema_version");
        addMetadata();
    }

    public QSchemaVersion(PathMetadata metadata) {
        super(SchemaVersion.class, metadata, "PUBLIC", "schema_version");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(checksum, ColumnMetadata.named("checksum").withIndex(6).ofType(Types.INTEGER).withSize(10));
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(200).notNull());
        addMetadata(executionTime, ColumnMetadata.named("execution_time").withIndex(9).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(installedBy, ColumnMetadata.named("installed_by").withIndex(7).ofType(Types.VARCHAR).withSize(100).notNull());
        addMetadata(installedOn, ColumnMetadata.named("installed_on").withIndex(8).ofType(Types.TIMESTAMP).withSize(23).withDigits(10).notNull());
        addMetadata(installedRank, ColumnMetadata.named("installed_rank").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(script, ColumnMetadata.named("script").withIndex(5).ofType(Types.VARCHAR).withSize(1000).notNull());
        addMetadata(success, ColumnMetadata.named("success").withIndex(10).ofType(Types.BOOLEAN).withSize(1).notNull());
        addMetadata(type, ColumnMetadata.named("type").withIndex(4).ofType(Types.VARCHAR).withSize(20).notNull());
        addMetadata(version, ColumnMetadata.named("version").withIndex(2).ofType(Types.VARCHAR).withSize(50));
    }

}

