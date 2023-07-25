package uz.community.javacommunity.controller.subArticleContent.data;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uz.community.javacommunity.common.JsonConverter;
import uz.community.javacommunity.controller.dto.SubArticleContentCreateRequest;
import uz.community.javacommunity.controller.dto.SubArticleContentResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Profile("functionalTest")
@RequiredArgsConstructor
public class TestDataHelperSubArticleContent {

    private static final String BASE_PATH = "/article/sub/content/";
    private final JsonConverter jsonConverter;
    private final MockMvc mockMvc;

    @SneakyThrows
    public RequestBuilder createSubArticleContentTextRequest(SubArticleContentCreateRequest request) {

        return post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convertToString(request));
    }

    public RequestBuilder createSubArticleContentImageRequest(UUID subArticleId, MockMultipartFile photo) {

        return MockMvcRequestBuilders
                .multipart(BASE_PATH + subArticleId)
                .file(photo);
    }

    public RequestBuilder getSubArticleContentRequest(UUID id) {
        return get(BASE_PATH + id);
    }

    @SneakyThrows
    public SubArticleContentResponse createSubArticleContent(SubArticleContentCreateRequest subArticle) {

        RequestBuilder request = createSubArticleContentTextRequest(subArticle);
        String subArticleContent = mockMvc.perform(request).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return jsonConverter.convertFromString(subArticleContent, SubArticleContentResponse.class);
    }

    @SneakyThrows
    public SubArticleContentResponse createSubArticleContent(UUID subArticleId, MockMultipartFile photo) {

        RequestBuilder request = createSubArticleContentImageRequest(subArticleId, photo);
        String subArticleContent = mockMvc.perform(request).andExpect(status()
                .isCreated()).andReturn().getResponse().getContentAsString();

        return jsonConverter.convertFromString(subArticleContent, SubArticleContentResponse.class);
    }

    @SneakyThrows
    public MockMultipartFile getImage() {

        ClassPathResource imageResource = new ClassPathResource("images/test.jpg");
        return new MockMultipartFile("photo",
                imageResource.getFilename(),
                MediaType.IMAGE_JPEG_VALUE,
                imageResource.getInputStream());
    }
}
