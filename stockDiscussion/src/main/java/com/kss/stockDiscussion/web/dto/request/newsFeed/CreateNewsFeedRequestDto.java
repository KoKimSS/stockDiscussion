package com.kss.stockDiscussion.web.dto.request.newsFeed;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Getter
@NoArgsConstructor
public class CreateNewsFeedRequestDto extends RequestDto {
    private User user;
    ActivityType activityType;
    private User relatedUser;
    private Poster relatedPoster;  // 해당 활동이 포함된 글

    @Builder
    private CreateNewsFeedRequestDto(User user, ActivityType activityType, User relatedUser, Poster relatedPoster) {
        this.user = user;
        this.activityType = activityType;
        this.relatedUser = relatedUser;
        this.relatedPoster = relatedPoster;
    }
}
