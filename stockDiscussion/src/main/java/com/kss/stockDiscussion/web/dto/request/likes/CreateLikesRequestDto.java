package com.kss.stockDiscussion.web.dto.request.likes;


import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateLikesRequestDto extends RequestDto {
    @NotBlank
    private Long userId;
    @NotBlank
    private Long posterId;
    @NotBlank
    private LikeType likeType;
    private Long replyId;

    @Builder
    private CreateLikesRequestDto(Long userId, Long posterId, LikeType likeType, Long replyId) {
        this.userId = userId;
        this.posterId = posterId;
        this.likeType = likeType;
        this.replyId = replyId;
    }
}
