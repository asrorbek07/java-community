package uz.community.javacommunity.controller.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("user")
public class User {
    @PrimaryKey
    private String username;
    @Frozen
    private Set<String> roles;
    @Column("created_date")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Instant createdDate;
    @Column("modified_date")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Instant modifiedDate;
    private int age;
    private String info;
    @Column("image_url")
    private String imageUrl;
}
