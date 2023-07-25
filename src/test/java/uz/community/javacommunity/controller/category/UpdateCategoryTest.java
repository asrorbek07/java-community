package uz.community.javacommunity.controller.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import uz.community.javacommunity.CommonIntegrationTest;
import uz.community.javacommunity.WithAuthentication;
import uz.community.javacommunity.controller.dto.*;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Update a category ( PUT /category/{id} )")
public class UpdateCategoryTest extends CommonIntegrationTest {
    @Test
    @DisplayName(value = "Should update a parent category with 200 status")
    @WithAuthentication(username = "owner")
    void shouldUpdateParentCategory() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        RequestBuilder request = testDataHelperCategory.updateCategoryRequest(
                category.getId(), "updated-category", null);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated-category"))
                .andExpect(jsonPath("$.modifiedDate").exists())
                .andExpect(jsonPath("$.parentId").doesNotExist());

        //GIVEN
        CategoryResponse category2 = testDataHelperCategory.createCategory("category", null);
        request = testDataHelperCategory.updateCategoryRequest(
                category2.getId(), "updated-category", category.getId());
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated-category"))
                .andExpect(jsonPath("$.modifiedDate").exists())
                .andExpect(jsonPath("$.parentId").value(category.getId().toString()));
    }

    @Test
    @DisplayName(value = "Should update a sub category with 200 status")
    @WithAuthentication(username = "owner")
    void shouldUpdateSubCategory() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        CategoryResponse subCategory = testDataHelperCategory.createCategory("sub-category", category.getId());
        RequestBuilder request = testDataHelperCategory.updateCategoryRequest(
                subCategory.getId(), "updated-category", category.getId());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated-category"))
                .andExpect(jsonPath("$.modifiedDate").exists())
                .andExpect(jsonPath("$.parentId").value(category.getId().toString()));
    }

    @Test
    @DisplayName("Should fail with 404 and 400 statuses if required property is not found or null value")
    @WithAuthentication(username = "owner")
    void shouldFailIfRequiredPropertyIsNotFoundOrInvalid() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        RequestBuilder request = testDataHelperCategory.updateCategoryRequest(
                category.getId(), "updated-category", UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());

        //GIVEN
        request = testDataHelperCategory.updateCategoryRequest(
                UUID.randomUUID(), "category", null);
        //WHEN
        resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(value = "Should fail with 409 if category already exists")
    @WithAuthentication(username = "owner")
    void shouldFailIfCategoryAlreadyExist() throws Exception {
        //GIVEN
        CategoryResponse category = testDataHelperCategory.createCategory("category", null);
        testDataHelperCategory.createCategory("category2", null);
        RequestBuilder request = testDataHelperCategory.updateCategoryRequest(
                category.getId(), "category2", null);
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(value = "Should fail with 403 status if user does not have authority")
    @WithAuthentication(username = "owner", roles = "ROLE_USER")
    void shouldFailUserIsNotAdmin() throws Exception {
        //GIVEN
        RequestBuilder request = testDataHelperCategory.updateCategoryRequest(
                UUID.randomUUID(), "category", UUID.randomUUID());
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
        RequestBuilder request = testDataHelperCategory.updateCategoryRequest(
                UUID.randomUUID(), "category", UUID.randomUUID());
        //WHEN
        ResultActions resultActions = mockMvc.perform(request);
        //THEN
        resultActions
                .andExpect(status().isUnauthorized());
    }
}
