package uz.community.javacommunity.controller.user;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Create a new user ( POST /user )")
class CreateUserTest extends CommonIntegrationTest {

    @Test
    @DisplayName("Should create a user with 201 status")
    void shouldCreateUser() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperUser.createUserRequest(
                "test_name", "test_password",
                "test_tenant_id", 10, Set.of("role1"));
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("test_name"))
                .andExpect(jsonPath("$.info").value("test_tenant_id"))
                .andExpect(jsonPath("$.age").value(10))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles", IsCollectionWithSize.hasSize(1)));
    }

    @Test
    @DisplayName("Should fail with 409 status if the user already exists")
    void shouldFailIfUserExists() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperUser.createUserRequest(
                "test_name", "test_password", "test_tenant_id",
                10, Set.of("role1"));
        //WHEN
        mockMvc.perform(request);
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should fail with 400 status if required fields are empty")
    void shouldFailIfEmptyRequiredField() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperUser.createUserRequest(
                "", "test_password", "test_tenant_id",
                10, Set.of("role1"));
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperUser.createUserRequest(
                "username", "", "test_tenant_id",
                10, Set.of("role1"));
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperUser.createUserRequest(
                "username_1", "password", "",
                10, Set.of("role1"));
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperUser.createUserRequest(
                "username_2", "password", "test_tenant_id",
                10, Set.of());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail with 400 status if username or password is invalid")
    void shouldFailIfInvalidUsernameOrPassword() throws Exception{
        //GIVEN
        RequestBuilder request = testDataHelperUser.createUserRequest(
                "you", "test_password",
                "test_tenant_id", 10, Set.of("role1"));
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperUser.createUserRequest(
                "test_username", "test",
                "test_tenant_id", 10, Set.of("role1"));
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }
}

