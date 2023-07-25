package uz.community.javacommunity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.community.javacommunity.controller.converter.LoginConverter;
import uz.community.javacommunity.controller.converter.UserConverter;
import uz.community.javacommunity.controller.domain.Login;
import uz.community.javacommunity.controller.domain.User;
import uz.community.javacommunity.controller.dto.*;
import uz.community.javacommunity.service.UserService;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "user")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    @Operation(summary = "Authenticate a person")
    public JwtTokenResponse userLogin(@RequestBody @Validated UserLoginRequest userLoginRequest) {
        Login login = LoginConverter.convertToEntity(userLoginRequest);
        String token = userService.login(login);
        return LoginConverter.from(token);
    }

    @GetMapping("/current")
    @Operation(summary = "Get current user info")
    UserResponse currentUser(Principal principal) {
        User user = userService.findByUserName(principal.getName());
        return UserConverter.from(user);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a user. Should be used only by BE team")
    public UserResponse createUser(@RequestBody @Validated UserCreateRequest userCreateRequest) {
        User user = UserConverter.convertToEntity(userCreateRequest);
        Login login = LoginConverter.convertToEntity(userCreateRequest);
        User savedUser = userService.create(user, login);
        return UserConverter.from(savedUser);
    }
}
