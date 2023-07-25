package uz.community.javacommunity.controller.subArticle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.ArticleResponse;
import uz.community.javacommunity.controller.dto.CategoryResponse;
import uz.community.javacommunity.controller.dto.SubArticleCreateRequest;
import uz.community.javacommunity.controller.dto.SubArticleResponse;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Delete subArticle ( DELETE /article/sub/{id} )")
class DeleteSubArticleTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should delete a SubArticle with 204 status")
    @WithAuthentication(username = "owner")
    void shouldDeleteSubArticle() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());

        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setArticleId(article.getId());
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setName("sub-article");
        SubArticleResponse subArticle = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);

        RequestBuilder request = testDataHelperSubArticle.deleteSubArticleRequest(subArticle.getId());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName(value = "Should fail with 404 status if the SubArticle cannot be found")
    @WithAuthentication(username = "owner")
    void shouldFailCategoryNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperSubArticle.deleteSubArticleRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Should fail with 403 status if user does not have authority")
    @WithAuthentication(username = "owner", roles = "ROlE_USER")
    void shouldFailUserIsNotAdmin() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperSubArticle.deleteSubArticleRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName(value = "Should fail with 401 status if not authorized")
    @WithAnonymousUser
    void shouldFailIfUnauthorized() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperSubArticle.deleteSubArticleRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }

}
