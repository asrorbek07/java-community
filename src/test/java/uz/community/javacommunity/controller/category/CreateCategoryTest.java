package uz.community.javacommunity.controller.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Create a new category ( POST /category )")
class CreateCategoryTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should create a parent category with 201 status")
    @WithAuthentication(username = "owner")
    void shouldCreateParentCategory() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperCategory
                .createCategoryRequest("category", null);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("category"))
                .andExpect(jsonPath("$.createdBy").value("owner"))
                .andExpect(jsonPath("$.parentId").doesNotExist());
    }

    @Test
    @DisplayName(value = "Should create a sub category with 201 status")
    @WithAuthentication(username = "owner")
    void shouldCreateSubCategory() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory
                .createCategory("category", null);
        RequestBuilder request = testDataHelperCategory
                .createCategoryRequest("sub-category", category.getId());
        //WHEN
        ResultActions perform = mockMvc.perform(request);
        //THEN
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdBy").value("owner"))
                .andExpect(jsonPath("$.name").value("sub-category"))
                .andExpect(jsonPath("$.parentId").value(category.getId().toString()))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.modifiedDate").exists())
                .andExpect(jsonPath("$.modifiedBy").value("owner"));
    }

    @Test
    @DisplayName(value = "Should fail with 409 status if category already exists")
    @WithAuthentication(username = "owner")
    void shouldFailIfCategoryExists() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        testDataHelperCategory.createCategory("category2", null);
        RequestBuilder request = testDataHelperCategory
                .createCategoryRequest("category2", null);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isConflict());

        //GIVEN
        testDataHelperCategory.createCategory("category1", category.getId());
        request = testDataHelperCategory.createCategoryRequest(
                "category1", category.getId());
        //WHEN
        mockMvc.perform(request);
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(value = "Should fail with 404 status if parent category cannot be found")
    @WithAuthentication(username = "owner")
    void shouldFailIfParentCategoryIdNotFound() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperCategory
                .createCategoryRequest("category", UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Should fail with 401 status if user is not authorized")
    @WithAnonymousUser
    void shouldThrowAuthenticationException() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperCategory
                .createCategoryRequest("test", null);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName(value = "Should get 400 status if fields Blank ")
    @WithAuthentication(username = "owner")
    void shouldThrowFieldValidationException() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperCategory
                .createCategoryRequest("", null);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isBadRequest());
    }
}
