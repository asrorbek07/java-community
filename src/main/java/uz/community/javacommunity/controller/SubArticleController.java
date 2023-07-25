package uz.community.javacommunity.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.community.javacommunity.controller.converter.SubArticleConverter;
import uz.community.javacommunity.controller.domain.SubArticle;
import uz.community.javacommunity.controller.dto.SubArticleCreateRequest;
import uz.community.javacommunity.controller.dto.SubArticleResponse;

import uz.community.javacommunity.controller.dto.SubArticleUpdateRequest;
import uz.community.javacommunity.service.SubArticleService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/article/sub")
@EnableMethodSecurity
@PreAuthorize("hasRole('ADMIN')")
public class SubArticleController {
    private final SubArticleService service;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubArticleResponse create(
            @RequestBody @Validated SubArticleCreateRequest subArticleCreateRequest,
            Principal principle)
    {
        SubArticle subArticle = SubArticleConverter.convertToEntity(subArticleCreateRequest);
        SubArticle savedSubArticle = service.create(subArticle, principle.getName());
        return SubArticleConverter.from(savedSubArticle);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubArticleResponse update(
            @PathVariable UUID id,
            @RequestBody @Validated SubArticleUpdateRequest subArticleUpdateRequest,
            Principal principal)
    {
        SubArticle subArticle =SubArticleConverter.convertToEntity(subArticleUpdateRequest);
        SubArticle updatedSubArticle = service.update(id,subArticle, principal.getName());
        return SubArticleConverter.from(updatedSubArticle);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    public List<SubArticleResponse> get(@PathVariable UUID id)
    {
        List<SubArticle> subArticles = service.getAllByArticleId(id);
        return SubArticleConverter.from(subArticles);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete entire SubArticle by Id")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id){
        service.delete(id);
    }

    @DeleteMapping("/byArticleId/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete entire SubArticle by ArticleId")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBYArticleId(@PathVariable UUID id){
        service.deleteBYArticleId(id);
    }
}
