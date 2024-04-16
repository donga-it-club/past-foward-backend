package aws.retrospective.dto;

import lombok.Getter;

@Getter
public record CommentDto(Long id, String content, int totalCount) {

}