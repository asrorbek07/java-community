package uz.community.javacommunity.controller.article;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.ArticleResponse;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Update an article ( PUT /article/{id} )")
class ArticleUpdateTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should update an Article with 200 status")
    @WithAuthentication(username = "owner")
    void shouldUpdateArticle() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        CategoryResponse category2 = testDataHelperCategory.createCategory("category2", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());
        RequestBuilder request = testDataHelperArticle.updateArticleRequest(
                article.getId(), category2.getId(), "updated-article");
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated-article"))
                .andExpect(jsonPath("$.categoryId").value(category2.getId().toString()));
    }

    @Test
    @DisplayName("Should fail with 404 and 400 statuses if required property is not found or null value")
    @WithAuthentication(username = "owner")
    void shouldFailIfRequiredPropertyIsNotFoundOrInvalid() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        RequestBuilder request = testDataHelperArticle.updateArticleRequest(
                UUID.randomUUID(), category.getId(), "article");
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());

        //GIVEN
        request = testDataHelperArticle.updateArticleRequest(
                UUID.randomUUID(), UUID.randomUUID(), "article");
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());

        //GIVEN
        request = testDataHelperArticle.updateArticleRequest(
                UUID.randomUUID(), null, "article");
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail with 403 status if user does not have authority")
    @WithAuthentication(username = "owner", roles = "ROLE_USER")
    void shouldFailUserIsNotAdmin() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.updateArticleRequest(
                UUID.randomUUID(), UUID.randomUUID(), "article");
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should fail with 401 status if user is not authorized")
    @WithAnonymousUser
    void shouldFailIfUnauthorized() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.updateArticleRequest(
                UUID.randomUUID(), UUID.randomUUID(), "article");
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }

}
