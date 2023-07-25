package uz.community.javacommunity.controller.converter;

import lombok.experimental.UtilityClass;
import uz.community.javacommunity.controller.domain.Article;
import uz.community.javacommunity.controller.dto.ArticleCreateRequest;
import uz.community.javacommunity.controller.dto.ArticleResponse;
import uz.community.javacommunity.controller.dto.ArticleUpdateRequest;

import java.util.List;

@UtilityClass
public class ArticleConverter {
    public ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .name(article.getName())
                .id(article.getId())
                .categoryId(article.getCategoryId())
                .createdBy(article.getCreatedBy())
                .createdDate(article.getCreatedDate().toString())
                .modifiedBy(article.getModifiedBy())
                .modifiedDate(article.getModifiedDate())
                .build();
    }

    public List<ArticleResponse> from(List<Article> articles) {
        return articles.stream().map(ArticleConverter::from).toList();
    }

    public Article convertToEntity(ArticleCreateRequest articleCreateRequest) {
        return Article.builder()
                .categoryId(articleCreateRequest.getCategoryId())
                .name(articleCreateRequest.getName())
                .build();
    }

    public Article convertToEntity(ArticleUpdateRequest articleUpdateRequest) {
        return Article.builder()
                .categoryId(articleUpdateRequest.getCategoryId())
                .name(articleUpdateRequest.getName())
                .build();
    }
}
