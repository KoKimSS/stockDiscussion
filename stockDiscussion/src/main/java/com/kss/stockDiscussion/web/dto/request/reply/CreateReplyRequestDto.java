package com.kss.stockDiscussion.web.dto.request.reply;

import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CreateReplyRequestDto extends RequestDto {
    @NotBlank
    private String contents;
    @NotBlank
    private Long posterId;
    @NotBlank
    private Long userId;
}
