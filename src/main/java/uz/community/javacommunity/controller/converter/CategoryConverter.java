package uz.community.javacommunity.controller.converter;

import lombok.experimental.UtilityClass;
import uz.community.javacommunity.controller.domain.Category;
import uz.community.javacommunity.controller.dto.ArticleResponse;
import uz.community.javacommunity.controller.dto.CategoryCreateRequest;
import uz.community.javacommunity.controller.dto.CategoryResponse;
import uz.community.javacommunity.controller.dto.CategoryUpdateRequest;

import java.util.List;

@UtilityClass
public class CategoryConverter {
    public CategoryResponse from(Category category, List<CategoryResponse> categories, List<ArticleResponse> articles) {
        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .createdBy(category.getCreatedBy())
                .createdDate(category.getCreatedDate())
                .modifiedDate(category.getModifiedDate())
                .modifiedBy(category.getModifiedBy())
                .parentId(category.getParentId())
                .articles(articles)
                .subCategories(categories)
                .build();
    }
    public CategoryResponse from(Category category) {
        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .createdBy(category.getCreatedBy())
                .createdDate(category.getCreatedDate())
                .modifiedDate(category.getModifiedDate())
                .modifiedBy(category.getModifiedBy())
                .parentId(category.getParentId())
                .build();
    }
    public List<CategoryResponse> from(List<Category> categoryList){
        return categoryList.stream().map(CategoryConverter::from).toList();
    }
    public Category convertToEntity(CategoryCreateRequest categoryCreateRequest){
        return Category.builder()
                .name(categoryCreateRequest.getName())
                .parentId(categoryCreateRequest.getParentId())
                .build();
    }

    public Category convertToEntity(CategoryUpdateRequest categoryUpdateRequest) {
        return Category.builder()
                .name(categoryUpdateRequest.getName())
                .parentId(categoryUpdateRequest.getParentId())
                .build();
    }
}
