package uz.community.javacommunity.controller.category.data;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import uz.community.javacommunity.common.JsonConverter;
import uz.community.javacommunity.controller.dto.CategoryResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Profile("functionalTest")
@RequiredArgsConstructor
public class TestDataHelperCategory {
    private static final String BASE_PATH = "/category";
    private static final String GET_SUB_CATEGORIES_PATH = BASE_PATH + "/sub/{id}";
    private static final String GET_ALL_PARENT_CATEGORIES_BY_ID = BASE_PATH + "/getAllChild/{id}";
    private final JsonConverter jsonConverter;
    private final MockMvc mockMvc;

    public RequestBuilder createCategoryRequest(String categoryName, UUID parentId) {

        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("name", categoryName);
        createRequest.put("parentId", parentId);

        return post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(createRequest));
    }

    public RequestBuilder getSubCategoriesRequest(UUID id) {
        return get(GET_SUB_CATEGORIES_PATH, id);
    }

    public RequestBuilder updateCategoryRequest(UUID id, String categoryName, UUID parentId) {

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("parentId", parentId);
        updateRequest.put("id", id);
        updateRequest.put("name", categoryName);

        return put(BASE_PATH+'/'+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(updateRequest));
    }

    public RequestBuilder getParentCategoriesRequest() {
        return get(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON);
    }

    public RequestBuilder getAllChildCategoryAndArticleByCategoryId(UUID id){
        return get(GET_ALL_PARENT_CATEGORIES_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON);
    }

    @SneakyThrows
    public CategoryResponse createCategory(String categoryName, UUID parentId) {

        RequestBuilder request = createCategoryRequest(categoryName, parentId);
        String category = mockMvc.perform(request).andExpect(status()
                .isCreated()).andReturn().getResponse().getContentAsString();

        return jsonConverter.convertFromString(category, CategoryResponse.class);
    }

    public void createMultipleCategories(String categoryName, UUID parentId, int amount) {
        IntStream.range(0, amount).mapToObj((i) -> {
            try {
                return createCategory(categoryName + 'u', parentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
