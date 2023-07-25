package uz.community.javacommunity.controller.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.community.javacommunity.controller.domain.Category;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse  {
    UUID id;
    String name;
    UUID parentId;
    String createdBy;
    Instant createdDate;
    String modifiedBy;
    Instant modifiedDate;
    List<ArticleResponse> articles;
    List<CategoryResponse> subCategories;
}
