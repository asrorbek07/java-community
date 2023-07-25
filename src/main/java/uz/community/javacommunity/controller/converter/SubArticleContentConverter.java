package uz.community.javacommunity.controller.converter;

import lombok.experimental.UtilityClass;
import uz.community.javacommunity.controller.domain.SubArticleContent;
import uz.community.javacommunity.controller.dto.SubArticleContentCreateRequest;
import uz.community.javacommunity.controller.dto.SubArticleContentResponse;
import uz.community.javacommunity.controller.dto.SubArticleContentUpdateRequest;

import java.util.List;

@UtilityClass
public class SubArticleContentConverter {
    public SubArticleContent convertToEntity(SubArticleContentCreateRequest subArticleContentCreateRequest) {
        return SubArticleContent.builder()
                .content(subArticleContentCreateRequest.getContent())
                .isParagraph(subArticleContentCreateRequest.isParagraph())
                .subArticleId(subArticleContentCreateRequest.getSubArticleId())
                .build();

    }

    public SubArticleContent convertToEntity(SubArticleContentUpdateRequest subArticleUpdateRequest) {
        return SubArticleContent.builder()
                .content(subArticleUpdateRequest.getContent())
                .isParagraph(subArticleUpdateRequest.isParagraph())
                .subArticleId(subArticleUpdateRequest.getSubArticleId())
                .build();

    }

    public SubArticleContentResponse from(SubArticleContent subArticleContent) {
        return SubArticleContentResponse.builder()
                .id(subArticleContent.getId())
                .subArticleId(subArticleContent.getSubArticleId())
                .content(subArticleContent.getContent())
                .isParagraph(subArticleContent.isParagraph())
                .createdBy(subArticleContent.getCreatedBy())
                .modifiedBy(subArticleContent.getModifiedBy())
                .createdDate(subArticleContent.getCreatedDate())
                .modifiedDate(subArticleContent.getModifiedDate())
                .build();
    }

    public List<SubArticleContentResponse> from(List<SubArticleContent> subArticleContents) {
        return subArticleContents.stream().map(SubArticleContentConverter::from).toList();
    }
}
