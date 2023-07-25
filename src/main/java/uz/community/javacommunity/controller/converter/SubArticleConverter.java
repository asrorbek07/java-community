package uz.community.javacommunity.controller.converter;

import lombok.experimental.UtilityClass;
import uz.community.javacommunity.controller.domain.SubArticle;
import uz.community.javacommunity.controller.dto.SubArticleCreateRequest;
import uz.community.javacommunity.controller.dto.SubArticleResponse;
import uz.community.javacommunity.controller.dto.SubArticleUpdateRequest;

import java.util.List;

@UtilityClass
public class SubArticleConverter {
    public SubArticle convertToEntity(SubArticleCreateRequest subArticleCreateRequest) {
        return SubArticle.builder()
                .name(subArticleCreateRequest.getName())
                .articleId(subArticleCreateRequest.getArticleId())
                .parentSubArticleId(subArticleCreateRequest.getParentSubArticleId())
                .build();
    }

    public SubArticle convertToEntity(SubArticleUpdateRequest subArticleUpdateRequest) {
        return SubArticle.builder()
                .name(subArticleUpdateRequest.getName())
                .articleId(subArticleUpdateRequest.getArticleId())
                .parentSubArticleId(subArticleUpdateRequest.getParentSubArticleId())
                .build();
    }

    public SubArticleResponse from(SubArticle subArticle) {
        return SubArticleResponse.builder()
                .articleId(subArticle.getArticleId())
                .id(subArticle.getId())
                .parentSubArticleId(subArticle.getParentSubArticleId())
                .createdDate(subArticle.getCreatedDate())
                .modifiedDate(subArticle.getModifiedDate())
                .createdBy(subArticle.getCreatedBy())
                .modifiedBy(subArticle.getModifiedBy())
                .name(subArticle.getName())
                .build();
    }

    public List<SubArticleResponse> from(List<SubArticle> subArticles) {
        return subArticles.stream().map(SubArticleConverter::from).toList();
    }
}
