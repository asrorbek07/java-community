package uz.community.javacommunity.controller.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table("article")
public class Article {
    @PrimaryKey
    UUID id;
    @Column("category_id")
    UUID categoryId;
    String name;
    @Column("created_by")
    String createdBy;
    @Column("created_date")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    Instant createdDate;
    @Column("modified_by")
    String modifiedBy;
    @Column("modified_date")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    Instant modifiedDate;
}

