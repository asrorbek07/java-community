package uz.community.javacommunity.controller.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table("sub_article_content")
public class SubArticleContent {
    @PrimaryKey
    @CassandraType(type = CassandraType.Name.UUID)
    UUID id;
    @Column("sub_article_id")
    @CassandraType(type = CassandraType.Name.UUID)
    UUID subArticleId;
    String content;
    @Column("is_paragraph")
    boolean isParagraph;
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
