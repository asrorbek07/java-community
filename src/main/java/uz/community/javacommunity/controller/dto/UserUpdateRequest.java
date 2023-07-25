package uz.community.javacommunity.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest extends BaseUserRequest{
    String username;
    String password;
    Set<String> roles;
    String info;
}
