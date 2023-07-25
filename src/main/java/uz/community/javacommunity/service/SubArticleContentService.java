package uz.community.javacommunity.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.community.javacommunity.controller.domain.SubArticleContent;
import uz.community.javacommunity.controller.repository.SubArticleContentRepository;
import uz.community.javacommunity.validation.CommonSchemaValidator;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubArticleContentService {

    private static final String BASE_IMAGE_PATH = "src/main/resources/";
    private final SubArticleContentRepository repository;
    private final CommonSchemaValidator commonSchemaValidator;

    public SubArticleContent create(SubArticleContent subArticleContent, String createdBy) {
        commonSchemaValidator.validateSubArticle(subArticleContent.getSubArticleId());
        Instant now = Instant.now();
        subArticleContent.setId(UUID.randomUUID());
        subArticleContent.setCreatedBy(createdBy);
        subArticleContent.setCreatedDate(now);
        subArticleContent.setModifiedBy(createdBy);
        subArticleContent.setModifiedDate(now);
        subArticleContent.setParagraph(true);
        return repository.save(subArticleContent);
    }

    public SubArticleContent create(UUID id, MultipartFile photo, String createdBy) {
        commonSchemaValidator.validateSubArticle(id);
        String image = addImage(photo);
        Instant now = Instant.now();
        SubArticleContent subArticleContent = SubArticleContent
                .builder()
                .id(UUID.randomUUID())
                .content(image)
                .subArticleId(id)
                .createdDate(now)
                .createdBy(createdBy)
                .modifiedDate(now)
                .modifiedBy(createdBy)
                .isParagraph(false)
                .build();
        return repository.save(subArticleContent);
    }

    @SneakyThrows
    private String addImage(MultipartFile photo) {
        if (Objects.isNull(photo) || photo.isEmpty()) {
            throw new IllegalArgumentException("Image property cannot be null or empty value ");
        }
        String imageUrl = String.format("images/%s.jpg", UUID.randomUUID());
        File file = new File(BASE_IMAGE_PATH + imageUrl);
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] mainContent = photo.getBytes();
            outputStream.write(mainContent);
        }
        return imageUrl;
    }

    public List<SubArticleContent> getContents(UUID id) {
        commonSchemaValidator.validateUUID(id, "subArticle");
        List<SubArticleContent> subArticleContents =
                repository.findAllBySubArticleId(id);
        if (subArticleContents.isEmpty()) {
            commonSchemaValidator.validateSubArticle(id);
        }
        return subArticleContents;
    }

    public void delete(UUID subArticleContentId) {
        commonSchemaValidator.validateSubArticleContent(subArticleContentId);
        repository.deleteById(subArticleContentId);
    }

    public void deleteBySubArticleId(UUID subArticleId) {
        commonSchemaValidator.validateSubArticle(subArticleId);
        repository.deleteAllBySubArticleId(subArticleId);
    }
}
