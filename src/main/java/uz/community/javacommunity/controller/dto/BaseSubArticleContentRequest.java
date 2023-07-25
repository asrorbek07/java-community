package uz.community.javacommunity.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseSubArticleContentRequest {
    @NotNull(message = "error.invalid.sub_article_id.not_null")
    UUID subArticleId;
    @NotBlank(message = "Content cannot not be blank")
    String content;
    boolean isParagraph;
}
