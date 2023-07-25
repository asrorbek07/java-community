package uz.community.javacommunity.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubArticleResponse {
    UUID id;
    UUID articleId;
    UUID parentSubArticleId;
    String name;
    String createdBy;
    Instant createdDate;
    String modifiedBy;
    Instant modifiedDate;
    List<SubArticleResponse> childSubArticleList;
    List<SubArticleContentResponse> subArticleContentResponses;
}
