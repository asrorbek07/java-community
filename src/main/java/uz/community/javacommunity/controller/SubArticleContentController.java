package uz.community.javacommunity.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.community.javacommunity.controller.converter.SubArticleContentConverter;
import uz.community.javacommunity.controller.domain.SubArticleContent;
import uz.community.javacommunity.controller.dto.SubArticleContentCreateRequest;
import uz.community.javacommunity.controller.dto.SubArticleContentResponse;
import uz.community.javacommunity.service.SubArticleContentService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@EnableMethodSecurity
@RequestMapping("/article/sub/content")
@PreAuthorize("hasRole('ADMIN')")
public class SubArticleContentController {
    private final SubArticleContentService service;

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create with sub article content with image")
    public SubArticleContentResponse addImage(
            @PathVariable(value = "id") UUID id,
            @RequestParam(value = "photo") MultipartFile photo,
            Principal principal)
    {
        SubArticleContent savedContent = service.create(id, photo, principal.getName());
        return SubArticleContentConverter.from(savedContent);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Sub Article Content")
    public SubArticleContentResponse create(
            @RequestBody @Validated SubArticleContentCreateRequest subArticleContentCreateRequest,
            Principal principal)
    {
        SubArticleContent subArticleContent =
                SubArticleContentConverter.convertToEntity(subArticleContentCreateRequest);
        SubArticleContent savedContent = service.create(subArticleContent, principal.getName());
        return SubArticleContentConverter.from(savedContent);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get content(s) of a sub article.")
    @PreAuthorize(value = "permitAll()")
    public List<SubArticleContentResponse> getSubArticleContents(
            @PathVariable UUID id)
    {
        List<SubArticleContent> subArticleContents = service.getContents(id);
        return SubArticleContentConverter.from(subArticleContents);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete SubArticleContent by Id")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public void delete(
            @PathVariable UUID subArticleContentId
    ){
        service.delete(subArticleContentId);
    }
    @DeleteMapping("/bySubArticleId/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete SubArticleContents by SubArticleId")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public void deleteBySubArticleId(
            @PathVariable UUID subArticleId
    ){
        service.deleteBySubArticleId(subArticleId);
    }
}
