package uz.community.javacommunity.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Log in a user ( POST /user/login )")
class UserLoginTest extends CommonIntegrationTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("Should log in a user with 200 status")
    void shouldLoginSuccessfully() throws Exception {
        //GIVEN
        testDataHelperUser.createUser(USERNAME, PASSWORD);
        RequestBuilder request = testDataHelperUser.loginRequest(USERNAME, PASSWORD);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 401 status if password is not correct")
    void shouldFailIfIncorrectPassword() throws Exception {
        //GIVEN
        testDataHelperUser.createUser(USERNAME, PASSWORD);
        RequestBuilder request = testDataHelperUser.loginRequest(USERNAME, "invalid_password");
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 400 status if login or password are empty")
    void shouldFailIfEmptyRequiredField() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperUser.loginRequest("", PASSWORD);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperUser.loginRequest(USERNAME, "");
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 401 status if user can not be found by username")
    void shouldFailIfUserNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperUser.loginRequest(USERNAME, PASSWORD);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 401 status if login can not be found by username")
    void shouldFailIfLoginNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperUser.loginRequest(USERNAME, PASSWORD);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }
}
