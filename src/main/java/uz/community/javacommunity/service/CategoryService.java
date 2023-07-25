package uz.community.javacommunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.community.javacommunity.common.exception.RecordNotFoundException;
import uz.community.javacommunity.controller.converter.ArticleConverter;
import uz.community.javacommunity.controller.converter.CategoryConverter;
import uz.community.javacommunity.controller.domain.Article;
import uz.community.javacommunity.controller.domain.Category;
import uz.community.javacommunity.controller.dto.CategoryResponse;
import uz.community.javacommunity.controller.repository.ArticleRepository;
import uz.community.javacommunity.controller.repository.CategoryRepository;
import uz.community.javacommunity.validation.CommonSchemaValidator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CommonSchemaValidator commonSchemaValidator;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    public Category create(Category category, String createdBy) {
        commonSchemaValidator.validateCategoryExistByNameAndParentID(
                category.getId(), category.getName(), category.getParentId());
        Instant now = Instant.now();
        category.setId(UUID.randomUUID());
        category.setCreatedBy(createdBy);
        category.setCreatedDate(now);
        category.setModifiedBy(createdBy);
        category.setModifiedDate(now);
        return categoryRepository.save(category);
    }

    public Category update(UUID id,Category category, String updatedBy) {
        Category categoryEntity = getById(id);
        commonSchemaValidator.validateCategoryExistByNameAndParentID(
                id, category.getName(), category.getParentId());
        categoryEntity.setName(category.getName());
        categoryEntity.setParentId(category.getParentId());
        categoryEntity.setModifiedDate(Instant.now());
        categoryEntity.setModifiedBy(updatedBy);
        return categoryRepository.save(categoryEntity);
    }

    public List<Category> getSubCategories(UUID parentCategoryId) {
        commonSchemaValidator.validateCategory(parentCategoryId);
        return categoryRepository.findAllByParentId(parentCategoryId);
    }

    public List<CategoryResponse> getCategoriesWithMembers() {
        List<CategoryResponse> categoryResponses = CategoryConverter.from(getMainParentCategories());

        categoryResponses.forEach(this::getCategoryContent);
        return categoryResponses;
    }

    private List<Category> getMainParentCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> category.getParentId() == null).toList();
    }

    public void getCategoryContent(CategoryResponse categoryResponse) {
        List<Article> articles = articleRepository.findAllByCategoryId(categoryResponse.getId());
        if (!articles.isEmpty()) {
            categoryResponse.setArticles(articles.stream().map(ArticleConverter::from).toList());
        }
        List<Category> categories = categoryRepository.findAllByParentId(categoryResponse.getId());
        if (!categories.isEmpty()) {
            List<CategoryResponse> categoryResponses = categories.stream().map(CategoryConverter::from).toList();
            categoryResponses.forEach(this::getCategoryContent);
            categoryResponse.setSubCategories(categoryResponses);
        }
    }

    public Category getById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException(String.format("Category not found for id %s", id)));
    }

    public void delete(UUID id) {
        commonSchemaValidator.validateCategory(id);
        articleService.deleteByCategoryId(id);
        List<Category> categories = categoryRepository.findAllByParentId(id);
        if (!categories.isEmpty()) {
            categories.forEach(category -> delete(category.getId()));
        }
        categoryRepository.deleteById(id);
    }

    public List<Category> listCategoriesByCategoryId(UUID categoryId) {
        return categoryRepository.findAllByParentId(categoryId);
    }
}
