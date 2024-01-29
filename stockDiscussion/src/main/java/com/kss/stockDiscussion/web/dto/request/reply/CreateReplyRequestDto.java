package com.kss.stockDiscussion.web.dto.request.reply;

import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CreateReplyRequestDto extends RequestDto {
    @NotBlank(message = ValidationMessage.NOT_BLANK_CONTENTS)
    private String contents;
    @NotNull(message = ValidationMessage.NOT_NULL_POSTER)
    private Long posterId;
    @NotNull(message = ValidationMessage.NOT_NULL_USER)
    private Long userId;

    @Builder
    private CreateReplyRequestDto(String contents, Long posterId, Long userId) {
        this.contents = contents;
        this.posterId = posterId;
        this.userId = userId;
    }
}
