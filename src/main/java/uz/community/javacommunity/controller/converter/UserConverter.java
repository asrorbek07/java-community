package uz.community.javacommunity.controller.converter;


import lombok.experimental.UtilityClass;
import uz.community.javacommunity.controller.domain.User;
import uz.community.javacommunity.controller.dto.UserCreateRequest;
import uz.community.javacommunity.controller.dto.UserResponse;
import uz.community.javacommunity.controller.dto.UserUpdateRequest;

import java.util.List;

@UtilityClass
public class UserConverter {

    public User convertToEntity(UserCreateRequest userCreateRequest) {
        return User.builder()
                .age(userCreateRequest.getAge())
                .info(userCreateRequest.getInfo())
                .roles(userCreateRequest.getRoles())
                .imageUrl(userCreateRequest.getImgUrl())
                .username(userCreateRequest.getUsername())
                .build();
    }

    public User convertToEntity(UserUpdateRequest userUpdateRequest) {
        return User.builder()
                .age(userUpdateRequest.getAge())
                .info(userUpdateRequest.getInfo())
                .roles(userUpdateRequest.getRoles())
                .imageUrl(userUpdateRequest.getImgUrl())
                .username(userUpdateRequest.getUsername())
                .build();
    }

    public UserResponse from(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .age(user.getAge())
                .imgUrl(user.getImageUrl())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .roles(user.getRoles())
                .info(user.getInfo())
                .build();
    }

    public List<UserResponse> from(List<User> users) {
        return users.stream().map(UserConverter::from).toList();
    }
}
