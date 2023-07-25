package uz.community.javacommunity.controller.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BaseSubArticleRequest {
    UUID articleId;
    UUID parentSubArticleId;
    @NotBlank(message = "error.invalid.name.not_blank")
    String name;
}
