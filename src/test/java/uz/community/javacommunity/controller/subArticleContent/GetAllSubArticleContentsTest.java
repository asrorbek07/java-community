package uz.community.javacommunity.controller.subArticleContent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.*;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Get all sub article contents ( GET /article/sub/content/{id} )")
class GetAllSubArticleContentsTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should get all sub article contents with 200 status")
    @WithAuthentication(username = "owner")
    void shouldGetAllSubArticleContents() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());

        SubArticleCreateRequest subArticleCreateRequest = new SubArticleCreateRequest();
        subArticleCreateRequest.setArticleId(article.getId());
        subArticleCreateRequest.setParentSubArticleId(null);
        subArticleCreateRequest.setName("parent-sub-article");
        SubArticleResponse subArticle = testDataHelperSubArticle.createSubArticle(subArticleCreateRequest);
        RequestBuilder request = testDataHelperSubArticleContent.getSubArticleContentRequest(subArticle.getId());

        //WHEN
        ResultActions resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$", hasSize(0)));


        //GIVEN
        MockMultipartFile image = testDataHelperSubArticleContent.getImage();
        testDataHelperSubArticleContent.createSubArticleContent(subArticle.getId(), image);
        request = testDataHelperSubArticleContent.getSubArticleContentRequest(subArticle.getId());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].subArticleId").value(subArticle.getId().toString()))
                .andExpect(jsonPath("$[0].content").exists())
                .andExpect(jsonPath("$[0].paragraph").value(false));

        //GIVEN
        SubArticleContentCreateRequest createRequest = new SubArticleContentCreateRequest();
        createRequest.setSubArticleId(subArticle.getId());
        createRequest.setContent("test-text");
        testDataHelperSubArticleContent
                .createSubArticleContent(createRequest);
        testDataHelperSubArticleContent
                .createSubArticleContent(createRequest);
        request = testDataHelperSubArticleContent
                .getSubArticleContentRequest(subArticle.getId());

        //WHEN
        resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName(value = "Should fail with 404 status if sub article cannot be found")
    @WithAuthentication(username = "owner")
    void shouldFailIfSubArticleNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperSubArticleContent
                .getSubArticleContentRequest(UUID.randomUUID());

        //WHEN
        ResultActions resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isNotFound());
    }
}
