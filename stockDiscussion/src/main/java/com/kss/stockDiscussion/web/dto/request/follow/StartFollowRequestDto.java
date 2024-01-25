package com.kss.stockDiscussion.web.dto.request.follow;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class StartFollowRequestDto extends RequestDto {
    @NotBlank
    private Long followerId;
    @NotBlank
    private Long followingId;
}
