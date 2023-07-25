package uz.community.javacommunity.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import uz.community.javacommunity.controller.converter.ArticleConverter;
import uz.community.javacommunity.controller.converter.CategoryConverter;
import uz.community.javacommunity.controller.domain.Article;
import uz.community.javacommunity.controller.domain.Category;
import uz.community.javacommunity.controller.dto.*;
import uz.community.javacommunity.service.ArticleService;
import uz.community.javacommunity.service.CategoryService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@EnableMethodSecurity
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {
    private final CategoryService categoryService;
    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse saveCategory(
            @RequestBody @Validated CategoryCreateRequest categoryCreateRequest,
            Principal principal)
    {
        Category category = CategoryConverter.convertToEntity(categoryCreateRequest);
        Category savedCategory = categoryService.create(category, principal.getName());
        return CategoryConverter.from(savedCategory);
    }

    @GetMapping("/sub/{id}")
    @PreAuthorize("permitAll()")
    public List<CategoryResponse> getSubCategories(@PathVariable UUID id)
    {
        List<Category> subCategories = categoryService.getSubCategories(id);
        return CategoryConverter.from(subCategories);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    public List<CategoryResponse> getAllCategories()
    {
        return categoryService.getCategoriesWithMembers();
    }

    @PutMapping("{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Update category")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(
            @PathVariable UUID id,
            @RequestBody @Validated CategoryUpdateRequest categoryUpdateRequest,
            Principal principal)
    {
        Category category = CategoryConverter.convertToEntity(categoryUpdateRequest);
        Category updatedCategory = categoryService.update(id,category, principal.getName());
        return CategoryConverter.from(updatedCategory);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "delete Category")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(
            @PathVariable UUID id
    ){
        categoryService.delete(id);
    }

    @ResponseStatus(OK)
    @GetMapping("getAllChild/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Output child categories when parent category is clicked")
    public CategoryResponse getAllChildCategoryAndChildArticleByCategoryId(
            @PathVariable UUID id
    ) {
        Category category = categoryService.getById(id);
        List<Category> categories = categoryService.listCategoriesByCategoryId(id);
        List<Article> articles = articleService.listArticlesByCategoryId(id);

        List<CategoryResponse> categoryResponses = CategoryConverter.from(categories);
        List<ArticleResponse> articleResponses = ArticleConverter.from(articles);
        return CategoryConverter.from(category, categoryResponses, articleResponses);
    }

}
