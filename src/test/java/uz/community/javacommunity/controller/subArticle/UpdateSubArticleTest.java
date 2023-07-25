package uz.community.javacommunity.controller.subArticle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.*;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("Update a sub article ( PUT /article/sub/{id} )")
class UpdateSubArticleTest extends CommonIntegrationTest {

    @Test
    @DisplayName(value = "Should update a sub article with 200 status")
    @WithAuthentication(username = "owner")
    void shouldUpdateSubArticle() throws Exception {
        //GIVEN
        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setParentSubArticleId(null);

        CategoryResponse category1 = testDataHelperCategory.createCategory("category-1", null);
        ArticleResponse article1 = testDataHelperArticle.createArticle("article-1", category1.getId());
        subArticleCreateRequest.setArticleId(article1.getId());
        subArticleCreateRequest.setName("category1-article1-subArticle1");
        SubArticleResponse subArticle1 = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);

        CategoryResponse category2 = testDataHelperCategory.createCategory("article-2", null);
        ArticleResponse article2 = testDataHelperArticle.createArticle("Article2", category2.getId());
        subArticleCreateRequest.setArticleId(article2.getId());
        subArticleCreateRequest.setName("category2-article2-subArticle2");
        SubArticleResponse subArticle2 = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);

        SubArticleUpdateRequest subArticleUpdateRequest = new SubArticleUpdateRequest();
        subArticleUpdateRequest.setParentSubArticleId(null);
        subArticleUpdateRequest.setArticleId(article2.getId());
        subArticleUpdateRequest.setName("category2-article2-subArticle1");
        RequestBuilder request1 = testDataHelperSubArticle.updateSubArticleRequest(
                subArticle1.getId(),subArticleUpdateRequest);

        subArticleUpdateRequest.setParentSubArticleId(null);
        subArticleUpdateRequest.setArticleId(article1.getId());
        subArticleUpdateRequest.setName("category1-article1-subArticle2");
        RequestBuilder request2 = testDataHelperSubArticle.updateSubArticleRequest(subArticle2.getId(),subArticleUpdateRequest);

        //WHEN
        ResultActions resultActions1 = mockMvc.perform(request1);
        ResultActions resultActions2 = mockMvc.perform(request2);
        //THEN
        resultActions1.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subArticle1.getId().toString()))
                .andExpect(jsonPath("$.name").value("category2-article2-subArticle1"))
                .andExpect(jsonPath("$.parentSubArticleId").doesNotExist())
                .andExpect(jsonPath("$.articleId").value(article2.getId().toString()));
        resultActions2.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subArticle2.getId().toString()))
                .andExpect(jsonPath("$.name").value("category1-article1-subArticle2"))
                .andExpect(jsonPath("$.parentSubArticleId").doesNotExist())
                .andExpect(jsonPath("$.articleId").value(article1.getId().toString()));
    }

    @Test
    @DisplayName("Should fail with 404 and 400 statuses if required property is not found or null value")
    @WithAuthentication(username = "owner")
    void shouldFailIfRequiredPropertyIsNotFoundOrInvalid() throws Exception {
        //GIVEN
        SubArticleUpdateRequest subArticleUpdateRequest = new SubArticleUpdateRequest();
        subArticleUpdateRequest.setParentSubArticleId(UUID.randomUUID());
        subArticleUpdateRequest.setArticleId(null);
        subArticleUpdateRequest.setName("sub-article");

        RequestBuilder request = testDataHelperSubArticle.updateSubArticleRequest(
                UUID.randomUUID(),subArticleUpdateRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isNotFound());

        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());

        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setArticleId(article.getId());
        subArticleCreateRequest.setName("sub-article");

        SubArticleResponse subArticle = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);

        subArticleUpdateRequest.setParentSubArticleId(null);
        subArticleUpdateRequest.setArticleId(null);
        subArticleUpdateRequest.setName("");

        request = testDataHelperSubArticle.updateSubArticleRequest(subArticle.getId(),subArticleUpdateRequest);
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail with 403 status if user does not have authority")
    @WithAuthentication(username = "owner", roles = "ROLE_USER")
    void shouldFailIfUserIsNotAdmin() throws Exception {
        //GIVEN
        SubArticleUpdateRequest subArticleUpdateRequest = new SubArticleUpdateRequest();
        subArticleUpdateRequest.setParentSubArticleId(UUID.randomUUID());
        subArticleUpdateRequest.setArticleId(UUID.randomUUID());
        subArticleUpdateRequest.setName("sub-article");

        RequestBuilder request = testDataHelperSubArticle.updateSubArticleRequest(
                UUID.randomUUID(),subArticleUpdateRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should fail with 401 status if user is not authorized")
    @WithAnonymousUser
    void shouldFailIfUnauthorized() throws Exception {
        //GIVEN
        SubArticleUpdateRequest subArticleUpdateRequest = new SubArticleUpdateRequest();
        subArticleUpdateRequest.setParentSubArticleId(UUID.randomUUID());
        subArticleUpdateRequest.setArticleId(UUID.randomUUID());
        subArticleUpdateRequest.setName("sub-article");

        RequestBuilder request = testDataHelperSubArticle.updateSubArticleRequest(
                UUID.randomUUID(),subArticleUpdateRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isUnauthorized());
    }
}
