package uz.community.javacommunity.controller.article.data;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import uz.community.javacommunity.common.JsonConverter;
import uz.community.javacommunity.controller.dto.ArticleResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Profile("functionalTest")
@RequiredArgsConstructor
public class TestDataHelperArticle {
    private static final String BASE_PATH = "/article/";
    private final JsonConverter jsonConverter;
    private final MockMvc mockMvc;

    public RequestBuilder getArticlesRequest(UUID categoryId) {
        return get(BASE_PATH + categoryId)
                .contentType(MediaType.APPLICATION_JSON);
    }

    public RequestBuilder createArticleRequest(String name, UUID categoryId) {

        Map<String, Object> createArticle = new HashMap<>();
        createArticle.put("name", name);
        createArticle.put("categoryId", categoryId);

        return post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(createArticle));
    }

    public RequestBuilder updateArticleRequest(UUID id, UUID categoryId, String name) {

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("categoryId", categoryId);
        updateRequest.put("name", name);

        return put(BASE_PATH+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(updateRequest));
    }

    public RequestBuilder deleteArticleRequest(UUID id) {
        return delete(BASE_PATH + id);
    }

    @SneakyThrows
    public ArticleResponse createArticle(String name, UUID categoryId){

        RequestBuilder request = createArticleRequest(name, categoryId);
        String contentAsString = mockMvc.perform(request).andExpect(status()
                .isCreated()).andReturn().getResponse().getContentAsString();

        return jsonConverter.convertFromString(contentAsString, ArticleResponse.class);
    }

}
