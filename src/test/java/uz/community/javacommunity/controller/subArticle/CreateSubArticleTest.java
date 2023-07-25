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

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Create a new sub article ( POST /article/sub )")
class CreateSubArticleTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should create a parent sub article with 201 status")
    @WithAuthentication(username = "owner")
    void shouldCreateParentSubArticle() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());
        SubArticleCreateRequest subArticle = new SubArticleCreateRequest();
        subArticle.setArticleId(article.getId());
        subArticle.setParentSubArticleId(null);
        subArticle.setName("sub-article");

        RequestBuilder request = testDataHelperSubArticle
                .createSubArticleRequest(subArticle);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("sub-article"))
                .andExpect(jsonPath("$.parentSubArticleId").doesNotExist())
                .andExpect(jsonPath("$.articleId").value(article.getId().toString()));
    }

    @Test
    @DisplayName(value = "Should create a sub article with 201 status")
    @WithAuthentication(username = "owner")
    void shouldCreateSubArticle() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());
        SubArticleCreateRequest subArticleRequest = new SubArticleCreateRequest();
        subArticleRequest.setArticleId(article.getId());
        subArticleRequest.setParentSubArticleId(null);
        subArticleRequest.setName("parent-sub-article");

        SubArticleResponse subArticle = testDataHelperSubArticle.createSubArticle(subArticleRequest);
        subArticleRequest.setName("sub-article");
        subArticleRequest.setArticleId(null);
        subArticleRequest.setParentSubArticleId(subArticle.getId());
        RequestBuilder request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("sub-article"))
                .andExpect(jsonPath("$.parentSubArticleId").value(subArticle.getId().toString()))
                .andExpect(jsonPath("$.articleId").doesNotExist());
    }

    @Test
    @DisplayName(value = "Should fail with 409 status if sub article name already exists")
    @WithAuthentication(username = "owner")
    void shouldFailIfAlreadyExist() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());

        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setArticleId(article.getId());
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setName("parent-sub-article");

        RequestBuilder request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleCreateRequest);
        //WHEN
        mockMvc.perform(request);
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isConflict());

        //GIVEN
        subArticleCreateRequest.setName("sub-article");
        SubArticleResponse subArticle = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);
        subArticleCreateRequest.setParentSubArticleId(subArticle.getId());
        subArticleCreateRequest.setArticleId(null);
        request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleCreateRequest);
        //WHEN
        mockMvc.perform(request);
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(value = "Should fail with 404 and 400 statuses if required property is not found or null")
    @WithAuthentication(username = "owner")
    void shouldFailIfRequiredPropertyIsNotFoundOrNull() throws Exception {
        //GIVEN
        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setArticleId(UUID.randomUUID());
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setName("parent-sub-article");

        RequestBuilder request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleCreateRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());

        //GIVEN
        subArticleCreateRequest.setArticleId(null);
        subArticleCreateRequest.setParentSubArticleId(UUID.randomUUID());
        request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleCreateRequest);
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());

        //GIVEN
        subArticleCreateRequest.setArticleId(null);
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setName("");
        request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleCreateRequest);
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail with 403 status if user does not have authority")
    @WithAuthentication(username = "owner", roles = "ROLE_USER")
    void shouldFailIfUserIsNotAdmin() throws Exception {
        //GIVEN
        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setArticleId(UUID.randomUUID());
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setName("parent-sub-article");

        RequestBuilder request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleCreateRequest);
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
        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setArticleId(UUID.randomUUID());
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setName("parent-sub-article");

        RequestBuilder request = testDataHelperSubArticle
                .createSubArticleRequest(subArticleCreateRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }
}
