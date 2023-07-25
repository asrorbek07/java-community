package uz.community.javacommunity.controller.subArticle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.controller.dto.*;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import java.util.UUID;

@DisplayName("Get all sub articles ( GET /article/sub/{id} )")
public class GetAllSubArticlesTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should get all sub articles with 200 status")
    void shouldGetSubArticle() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        ArticleResponse article = testDataHelperArticle.createArticle("article", category.getId());
        RequestBuilder request = testDataHelperSubArticle.getSubArticles(article.getId());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$").isEmpty());

        //GIVEN
        SubArticleCreateRequest subArticle = new SubArticleCreateRequest();
        subArticle.setArticleId(article.getId());
        subArticle.setParentSubArticleId(null);

        subArticle.setName("sub-article");
        testDataHelperSubArticle.createSubArticle(subArticle);

        subArticle.setName("sub-article-2");
        testDataHelperSubArticle.createSubArticle(subArticle);

        subArticle.setName("sub-article-3");
        testDataHelperSubArticle.createSubArticle(subArticle);

        request = testDataHelperSubArticle.getSubArticles(article.getId());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Should fail with 404 status if article cannot be found")
    void shouldFailArticleNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperSubArticle.getSubArticles(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isNotFound());
    }
}
