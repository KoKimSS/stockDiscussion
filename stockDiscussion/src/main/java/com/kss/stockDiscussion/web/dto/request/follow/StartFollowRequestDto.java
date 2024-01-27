package com.kss.stockDiscussion.web.dto.request.follow;

import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.kss.stockDiscussion.common.ValidationMessage.*;

@Getter
@NoArgsConstructor
public class StartFollowRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_FOLLOWER)
    private Long followerId;
    @NotNull(message = NOT_NULL_FOLLOWING)
    private Long followingId;

    @Builder
    private StartFollowRequestDto(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
