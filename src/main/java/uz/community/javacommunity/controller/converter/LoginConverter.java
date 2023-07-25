package uz.community.javacommunity.controller.converter;

import lombok.experimental.UtilityClass;
import uz.community.javacommunity.controller.domain.Login;
import uz.community.javacommunity.controller.dto.UserLoginRequest;
import uz.community.javacommunity.controller.dto.JwtTokenResponse;
import uz.community.javacommunity.controller.dto.UserCreateRequest;

@UtilityClass
public class LoginConverter {
    public Login convertToEntity(UserCreateRequest userCreateRequest) {
        return Login
                .builder()
                .username(userCreateRequest.getUsername())
                .password(userCreateRequest.getPassword())
                .build();
    }

    public Login convertToEntity(UserLoginRequest userLoginRequest) {
        return Login
                .builder()
                .username(userLoginRequest.getUsername())
                .password(userLoginRequest.getPassword())
                .build();
    }

    public JwtTokenResponse from(String token) {
        return JwtTokenResponse
                .builder()
                .token(token)
                .build();
    }
}
