package uz.community.javacommunity.controller.subArticle.data;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import uz.community.javacommunity.common.JsonConverter;
import uz.community.javacommunity.controller.dto.SubArticleCreateRequest;
import uz.community.javacommunity.controller.dto.SubArticleResponse;
import uz.community.javacommunity.controller.dto.SubArticleUpdateRequest;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Profile("functionalTest")
@RequiredArgsConstructor
public class TestDataHelperSubArticle {
    private static final String BASE_PATH = "/article/sub/";
    private final JsonConverter jsonConverter;
    private final MockMvc mockMvc;

    public RequestBuilder createSubArticleRequest(SubArticleCreateRequest createRequest) {

        return post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(createRequest));
    }

    public RequestBuilder updateSubArticleRequest(UUID id, SubArticleUpdateRequest updateRequest) {

        return put(BASE_PATH + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(updateRequest));
    }

    public RequestBuilder deleteSubArticleRequest(UUID id) {
        return delete(BASE_PATH + id);
    }

    public RequestBuilder getSubArticles(UUID id) {
        return get(BASE_PATH + id)
                .contentType(MediaType.APPLICATION_JSON);
    }

    @SneakyThrows
    public SubArticleResponse createSubArticle(SubArticleCreateRequest createRequest) {

        RequestBuilder request = createSubArticleRequest(createRequest);
        String contentAsString = mockMvc.perform(request).andExpect(status()
                .isCreated()).andReturn().getResponse().getContentAsString();

        return jsonConverter.convertFromString(contentAsString, SubArticleResponse.class);
    }
}
