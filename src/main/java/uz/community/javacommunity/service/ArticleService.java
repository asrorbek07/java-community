package uz.community.javacommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.community.javacommunity.common.exception.RecordNotFoundException;
import uz.community.javacommunity.controller.converter.ArticleConverter;
import uz.community.javacommunity.controller.converter.SubArticleContentConverter;
import uz.community.javacommunity.controller.converter.SubArticleConverter;
import uz.community.javacommunity.controller.domain.Article;
import uz.community.javacommunity.controller.domain.SubArticle;
import uz.community.javacommunity.controller.domain.SubArticleContent;
import uz.community.javacommunity.controller.dto.ArticleResponse;
import uz.community.javacommunity.controller.dto.SubArticleResponse;
import uz.community.javacommunity.controller.repository.ArticleRepository;
import uz.community.javacommunity.controller.repository.SubArticleContentRepository;
import uz.community.javacommunity.controller.repository.SubArticleRepository;
import uz.community.javacommunity.validation.CommonSchemaValidator;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final SubArticleRepository subArticleRepository;
    private final CommonSchemaValidator commonSchemaValidator;
    private final SubArticleContentRepository subArticleContentRepository;
    private final SubArticleService subArticleService;

    public Article create(Article article, String currentUser) {
        commonSchemaValidator.validateArticleExistByNameAndParentID(
                article.getId(), article.getName(), article.getCategoryId());
        Instant now = Instant.now();
        article.setId(UUID.randomUUID());
        article.setModifiedBy(currentUser);
        article.setModifiedDate(now);
        article.setCreatedBy(currentUser);
        article.setCreatedDate(now);
        return articleRepository.save(article);
    }

    public Article update(UUID id,Article article, String username) {
        Article articleEntity = getById(id);
        commonSchemaValidator.validateArticleExistByNameAndParentID(
                id, article.getName(), article.getCategoryId());
        articleEntity.setName(article.getName());
        articleEntity.setCategoryId(article.getCategoryId());
        articleEntity.setModifiedBy(username);
        articleEntity.setModifiedDate(Instant.now());
        return articleRepository.save(articleEntity);
    }

    public ArticleResponse getArticleById(UUID id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            ArticleResponse articleResponse = ArticleConverter.from(optionalArticle.get());
            getSubArticlesContentByArticle(articleResponse);
            return articleResponse;
        }
        return null;
    }

    private void getSubArticlesContentByArticle(ArticleResponse article) {
        List<SubArticle> subArticles = subArticleRepository.findAllByArticleId(article.getId());
        if (!subArticles.isEmpty()) {
            List<SubArticleResponse> subArticleResponses = subArticles.stream().map(SubArticleConverter::from).toList();
            subArticleResponses.forEach(this::getSubArticlesContentBySubArticle);
            article.setSubArticleResponseList(subArticleResponses);
        }
    }

    public List<Article> getAllByCategoryId(UUID categoryId) {
        commonSchemaValidator.validateCategory(categoryId);
        return articleRepository.findAllByCategoryId(categoryId);
    }

    public void delete(UUID id) {
        commonSchemaValidator.validateArticle(id);
        subArticleService.deleteBYArticleId(id);
        articleRepository.deleteById(id);
    }

    private void getSubArticlesContentBySubArticle(SubArticleResponse subArticle) {
        List<SubArticleContent> subArticleContents = subArticleContentRepository.findAllBySubArticleId(subArticle.getId());
        if (!subArticleContents.isEmpty()) {
            subArticle.setSubArticleContentResponses(SubArticleContentConverter.from(subArticleContents));
        }
        List<SubArticle> subArticles = subArticleRepository.findAllByParentSubArticleId(subArticle.getId());
        if (!subArticles.isEmpty()) {
            List<SubArticleResponse> list = subArticles.stream().map(SubArticleConverter::from).toList();
            list.forEach(this::getSubArticlesContentBySubArticle);
            subArticle.setChildSubArticleList(list);
        }
    }

    public List<Article> listArticlesByCategoryId(UUID categoryId){
        return articleRepository.findAllByCategoryId(categoryId);
    }

    private Article getById(UUID id) {
        return articleRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException(String.format("Article not found for id %s", id)));
    }

    public void deleteByCategoryId(UUID id) {
        List<Article> articles = articleRepository.findAllByCategoryId(id);
        if (!articles.isEmpty()) {
            articles.stream().forEach(article -> delete(article.getId()));
        }
    }
}
