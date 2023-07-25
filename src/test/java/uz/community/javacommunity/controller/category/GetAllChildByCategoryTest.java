package uz.community.javacommunity.controller.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("get all child categiries and articles by categoryId ( GET /category/getAllChild/{id} )")
public class GetAllChildByCategoryTest extends CommonIntegrationTest {

    @Test
    @DisplayName("should get all categories and articles by categoryId with 200 status")
    @WithAuthentication(username = "owner")
    void shouldGetAllCategoriesAndArticlesByCategoryId() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        RequestBuilder request = testDataHelperArticle.getArticlesRequest(category.getId());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$").isEmpty());
        //GIVEN
        testDataHelperArticle.createArticle("article1", category.getId());
        testDataHelperArticle.createArticle("article2", category.getId());
        testDataHelperArticle.createArticle("article3", category.getId());
        request= testDataHelperCategory.getAllChildCategoryAndArticleByCategoryId(category.getId());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    @DisplayName("Should fail with 404 status if categoryId cannot be found")
    void shouldFailIfCategoryIdNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperArticle.getArticlesRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isNotFound());
    }


}
