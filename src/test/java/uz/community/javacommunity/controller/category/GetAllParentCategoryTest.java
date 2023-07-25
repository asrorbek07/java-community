package uz.community.javacommunity.controller.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Get all Parent category ( GET /category/)")
class GetAllParentCategoryTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should get All Parent Categories with 200 status")
    void shouldGetAllParentCategories() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperCategory.getParentCategoriesRequest();
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$").isEmpty());

        //GIVEN
        CategoryResponse categoryResponse1 = testDataHelperCategory
                .createCategory("Kotlin Overview", null);
        CategoryResponse categoryResponse2 = testDataHelperCategory
                .createCategory("Iphone", null);

        testDataHelperCategory.createMultipleCategories("Si", categoryResponse1.getId(), 3);
        testDataHelperCategory.createMultipleCategories("Siu", categoryResponse2.getId(), 3);

        testDataHelperArticle.createArticle("Kotlin Multiplatform", categoryResponse1.getId());
        testDataHelperArticle.createArticle("Kotlin Native", categoryResponse1.getId());
        testDataHelperArticle.createArticle("Kotlin for Android", categoryResponse1.getId());

        testDataHelperArticle.createArticle("Kotlin Multiplatform", categoryResponse2.getId());
        testDataHelperArticle.createArticle("Kotlin Native", categoryResponse2.getId());
        testDataHelperArticle.createArticle("Kotlin for Android", categoryResponse2.getId());

        request = testDataHelperCategory.getParentCategoriesRequest();

        //WHEN
        resultActions = mockMvc.perform(request);

        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].articles").isArray())
                .andExpect(jsonPath("$[0].articles", hasSize(3)))
                .andExpect(jsonPath("$[1].articles").isArray())
                .andExpect(jsonPath("$[1].articles", hasSize(3)))
                .andExpect(jsonPath("$[0].subCategories").isArray())
                .andExpect(jsonPath("$[0].subCategories", hasSize(3)))
                .andExpect(jsonPath("$[1].subCategories").isArray())
                .andExpect(jsonPath("$[1].subCategories", hasSize(3)));
    }
}
