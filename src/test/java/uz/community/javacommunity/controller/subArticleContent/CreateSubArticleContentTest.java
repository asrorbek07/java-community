package uz.community.javacommunity.controller.subArticleContent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.*;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Create a new sub article content ( POST /article/sub/content )")
class CreateSubArticleContentTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should create a new sub article content with 201 status")
    @WithAuthentication(username = "owner")
    void shouldCreateSubArticleContentFile() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory
                .createCategory("category", null);
        ArticleResponse article = testDataHelperArticle
                .createArticle("article", category.getId());
        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setArticleId(article.getId());
        subArticleCreateRequest.setName("sub-article");
        SubArticleResponse subArticle = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);

        SubArticleContentCreateRequest createRequest = new SubArticleContentCreateRequest();
        createRequest.setSubArticleId(subArticle.getId());
        createRequest.setContent("test-text");
        RequestBuilder request = testDataHelperSubArticleContent
        .createSubArticleContentTextRequest(createRequest);

        //WHEN
        ResultActions resultActions = mockMvc.perform(request);

        //THEN
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.subArticleId").value(subArticle.getId().toString()))
                .andExpect(jsonPath("$.content").value("test-text"))
                .andExpect(jsonPath("$.paragraph").value(true));

        //GIVEN
        MockMultipartFile image = testDataHelperSubArticleContent.getImage();
        request = testDataHelperSubArticleContent.createSubArticleContentImageRequest(subArticle.getId(), image);
        //WHEN
        resultActions = mockMvc.perform(request);

        //THEN
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.subArticleId").value(subArticle.getId().toString()))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.paragraph").value(false));
    }

    @Test
    @DisplayName(value = "Should fail with 400 status if required property is null or empty")
    @WithAuthentication(username = "owner")
    void shouldFailIfRequiredPropertyIsNull() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory
                .createCategory("category", null);
        ArticleResponse article = testDataHelperArticle
                .createArticle("article", category.getId());
        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setArticleId(article.getId());
        subArticleCreateRequest.setName("sub-article");
        SubArticleResponse subArticle = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);

        SubArticleContentCreateRequest createRequest = new SubArticleContentCreateRequest();
        createRequest.setSubArticleId(null);
        createRequest.setContent(null);

        RequestBuilder request = testDataHelperSubArticleContent
                .createSubArticleContentTextRequest(createRequest);

        //WHEN
        ResultActions resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isBadRequest());

        //GIVEN
        request = testDataHelperSubArticleContent.createSubArticleContentImageRequest(
                subArticle.getId(), new MockMultipartFile("photo", new byte[0]));
        resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "Should fail with 404 status if one or more required IDs cannot be found")
    @WithAuthentication(username = "owner")
    void shouldFailIfRequiredIdCannotBeFound() throws Exception {
        //GIVEN
        SubArticleContentCreateRequest createRequest = new SubArticleContentCreateRequest();
        createRequest.setSubArticleId(UUID.randomUUID());
        createRequest.setContent("test-text");

        RequestBuilder request = testDataHelperSubArticleContent
                .createSubArticleContentTextRequest(createRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Should fail with 403 status if user is not admin")
    @WithAuthentication(username = "owner", roles = "ROLE_USER")
    void shouldFailIfNotAdmin() throws Exception {
        //GIVEN
        SubArticleContentCreateRequest createRequest = new SubArticleContentCreateRequest();
        createRequest.setSubArticleId(UUID.randomUUID());
        createRequest.setContent("test-text");

        RequestBuilder request = testDataHelperSubArticleContent
                .createSubArticleContentTextRequest(createRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName(value = "Should fail with 401 status if user is not authorized")
    @WithAnonymousUser
    void shouldFailIfUnauthorized() throws Exception {
        //GIVEN
        SubArticleContentCreateRequest createRequest = new SubArticleContentCreateRequest();
        createRequest.setSubArticleId(UUID.randomUUID());
        createRequest.setContent("test-text");

        RequestBuilder request = testDataHelperSubArticleContent
                .createSubArticleContentTextRequest(createRequest);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }
}
