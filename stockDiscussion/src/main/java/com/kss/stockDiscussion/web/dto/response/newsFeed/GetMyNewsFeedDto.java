package com.kss.stockDiscussion.web.dto.response.newsFeed;


import com.kss.stockDiscussion.domain.activity.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMyNewsFeedDto {
    private Long userId;
    private String userName;
    private Long activityUserId;
    private String activityUserName;
    private Long relatedUserId;
    private String relatedUserName;
    private Long relatedPosterId;
    private String relatedPosterName;
    private NewsFeedType newsFeedType;
    private String message;

    @Builder
    private GetMyNewsFeedDto(Long userId, String userName, Long activityUserId, String activityUserName, Long relatedUserId, String relatedUserName, Long relatedPosterId, String relatedPosterName, NewsFeedType newsFeedType) {
        this.userId = userId;
        this.userName = userName;
        this.activityUserId = activityUserId;
        this.activityUserName = activityUserName;
        this.relatedUserId = relatedUserId;
        this.relatedUserName = relatedUserName;
        this.relatedPosterId = relatedPosterId;
        this.relatedPosterName = relatedPosterName;
        this.newsFeedType = newsFeedType;
    }
}
