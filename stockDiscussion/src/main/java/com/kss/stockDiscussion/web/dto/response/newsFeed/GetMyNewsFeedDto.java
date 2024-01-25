package com.kss.stockDiscussion.web.dto.response.newsFeed;


import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMyNewsFeedDto {
    private Long activityUserId;
    private String activityUserName;
    private Long relatedUserId;
    private String relatedUserName;
    private Long relatedPosterId;
    private String relatedPosterName;
    private ActivityType activityType;
    private String message;

    public GetMyNewsFeedDto(Long activityUserId, String activityUserName, Long relatedUserId, String relatedUserName, Long relatedPosterId, String relatedPosterName, ActivityType activityType) {
        this.activityUserId = activityUserId;
        this.activityUserName = activityUserName;
        this.relatedUserId = relatedUserId;
        this.relatedUserName = relatedUserName;
        this.relatedPosterId = relatedPosterId;
        this.relatedPosterName = relatedPosterName;
        this.activityType = activityType;
        this.message = MessageMapper();
    }

    private String MessageMapper() {
        if(activityType==ActivityType.FOLLOW){
            return activityUserName + "님이 나를 팔로우 합니다";
        }
        if(activityType==ActivityType.REPLY){
            return activityUserName + "님이 나의 " + relatedPosterName + " 글에 답글을 달았습니다.";
        }
        if(activityType==ActivityType.LIKE){
            return activityUserName + "님이 나의 " + relatedPosterName + " 글에 좋아요를 눌렀습니다.";
        }
        return "";
    }
}
