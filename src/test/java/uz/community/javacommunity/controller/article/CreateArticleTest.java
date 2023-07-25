package uz.community.javacommunity.controller.article;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Create a new article ( POST /article )")
class CreateArticleTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should be success with 201 status, create a new Article")
    @WithAuthentication(username = "owner")
    void shouldCreateArticle() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        RequestBuilder request = testDataHelperArticle.createArticleRequest(
                "article", category.getId());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("article"))
                .andExpect(jsonPath("$.categoryId").value(category.getId().toString()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdDate").exists());
    }

    @Test
    @DisplayName(value = "Should fail with 409 status if the article already exists")
    @WithAuthentication(username = "owner")
    void shouldFailArticleExists() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        RequestBuilder request = testDataHelperArticle.createArticleRequest(
                "article", category.getId());
        //WHEN
        mockMvc.perform(request);
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(value = "Should fail with 404 status if the category cannot be found")
    @WithAuthentication(username = "owner")
    void shouldFailCategoryNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.createArticleRequest(
                "article", UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Should fail with 400 status if required fields are empty")
    @WithAuthentication(username = "owner")
    void shouldFailIfEmptyRequiredField() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.createArticleRequest(
                "article", null);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperArticle.createArticleRequest("", UUID.randomUUID());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperArticle.createArticleRequest(null, UUID.randomUUID());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperArticle.createArticleRequest("", null);
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "Should fail with 403 status if user does not have authority")
    @WithAuthentication(username = "owner", roles = "ROLE_USER")
    void shouldFailUserIsNotAdmin() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.createArticleRequest(
                "article", UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName(value = "Should fail with 401 status code if not authorized")
    @WithAnonymousUser
    void shouldFailIfUnauthorized() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.createArticleRequest(
                "article", UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }

}
