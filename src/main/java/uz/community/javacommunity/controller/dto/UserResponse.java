package uz.community.javacommunity.controller.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.community.javacommunity.controller.domain.User;

import java.time.Instant;
import java.util.Set;
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String username;
    Set<String> roles;
    String info;
    String imgUrl;
    Integer age;
    Instant createdDate;
    Instant modifiedDate;
}
