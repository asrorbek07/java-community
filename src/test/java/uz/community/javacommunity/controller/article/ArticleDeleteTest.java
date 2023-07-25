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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Delete an article ( DELETE /article/{id} )")
class ArticleDeleteTest extends CommonIntegrationTest {

    @Test
    @DisplayName(value = "Should delete an Article with 204 status ")
    @WithAuthentication(username = "owner")
    void shouldDeleteArticle() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());
        RequestBuilder request = testDataHelperArticle.deleteArticleRequest(article.getId());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName(value = "Should fail with 404 status if article cannot be found ")
    @WithAuthentication(username = "owner")
    void shouldFailIfArticleNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.deleteArticleRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Should fail with 403 status if user does not have authority")
    @WithAuthentication(username = "owner", roles = "ROLE_USER")
    void shouldFailUserIsNotAdmin() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.deleteArticleRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName(value = "Should fail with 401 status if user is not authorized")
    @WithAnonymousUser
    void shouldFailUserIsUnauthorized() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.deleteArticleRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }
}