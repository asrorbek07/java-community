package uz.community.javacommunity.controller.user.data;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import uz.community.javacommunity.common.JsonConverter;
import uz.community.javacommunity.controller.dto.UserResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Profile("functionalTest")
@RequiredArgsConstructor
public class TestDataHelperUser {
    private static final String BASE_PATH = "/user/";
    private static final String LOGIN_PATH = BASE_PATH + "login";
    private static final String GET_CURRENT_USER_PATH = BASE_PATH + "current";
    private final JsonConverter jsonConverter;
    private final MockMvc mockMvc;

    public RequestBuilder createUserRequest(
            String username, String password,
            String info, int age, Set<String> roles) {

        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("username", username);
        createRequest.put("password", password);
        createRequest.put("roles", roles);
        createRequest.put("info", info);
        createRequest.put("age", age);

        return post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(createRequest));
    }

    public RequestBuilder loginRequest(String username, String password) {

        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        return post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(loginRequest));
    }

    public RequestBuilder getCurrentUser() {
        return get(GET_CURRENT_USER_PATH);
    }

    @SneakyThrows
    public UserResponse createUser(String username, String password) {

        RequestBuilder request = createUserRequest(
                username, password, "tenant_id", 10, Set.of("role"));
        String jsonResponse = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return jsonConverter.convertFromString(jsonResponse, UserResponse.class);
    }

}
