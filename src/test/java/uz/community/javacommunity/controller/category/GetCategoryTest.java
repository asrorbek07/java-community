package uz.community.javacommunity.controller.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Get a category/categories ( GET/category/sub )")
class GetCategoryTest extends CommonIntegrationTest {

    @Test
    @DisplayName(value = "Should get all SubCategories by parentCategoryId with 200 status")
    void shouldGetAllSubCategories() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        RequestBuilder request = testDataHelperCategory.getSubCategoriesRequest(category.getId());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$").isEmpty());

        //GIVEN
        testDataHelperCategory.createCategoryRequest("sub-category-1", category.getId());
        testDataHelperCategory.createCategoryRequest("sub-category-2", category.getId());
        testDataHelperCategory.createCategoryRequest("sub-category-3", category.getId());
        testDataHelperCategory.createCategoryRequest("sub-category-4", category.getId());

        request = testDataHelperCategory.getSubCategoriesRequest(category.getId());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName(value = "Should fail with 404 status if parentCategoryId cannot be found")
    void shouldFailIfParentCategoryIdNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperCategory.getSubCategoriesRequest(UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions.andExpect(status().isNotFound());
    }

}