package uz.community.javacommunity.controller.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubArticleContentResponse {
    UUID id;
    UUID subArticleId;
    String content;
    boolean isParagraph;
    String createdBy;
    Instant createdDate;
    String modifiedBy;
    Instant modifiedDate;
}
