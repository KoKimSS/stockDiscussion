package com.kss.stockDiscussion.web.dto.request.newsFeed;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateNewsFeedRequestDto extends RequestDto {
    @NotBlank
    private Long activityUserId;
    @NotBlank
    private ActivityType activityType;  // 활동 타입
    Long relatedUserId; // 해당 활동과 관련된 유저의 아이디
    private Long posterId;  // 해당 활동의 posterId

    @Builder
    private CreateNewsFeedRequestDto(Long activityUserId, ActivityType activityType, Long posterId,Long relatedUserId) {
        this.relatedUserId = relatedUserId;
        this.activityUserId = activityUserId;
        this.activityType = activityType;
        this.posterId = posterId;
    }
}
