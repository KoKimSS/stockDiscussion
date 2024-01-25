package com.kss.stockDiscussion.web.dto.response.newsFeed;


import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetFollowingNewsFeedDto {
    private Long activityUserId;
    private String activityUserName;
    private Long relatedUserId;
    private String relatedUserName;
    private Long relatedPosterId;
    private String relatedPosterName;
    private ActivityType activityType;
    private String message;

    @Builder
    public GetFollowingNewsFeedDto(Long activityUserId, String activityUserName, Long relatedUserId, String relatedUserName, Long relatedPosterId, String relatedPosterName, ActivityType activityType) {
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
            return activityUserName + "님이 "+relatedUserName+"를 팔로우 합니다";
        }
        if(activityType==ActivityType.REPLY){
            return activityUserName + "님이 "+relatedUserName+" 님의 " + relatedPosterName + " 글에 답글을 달았습니다.";
        }
        if(activityType==ActivityType.LIKE){
            return activityUserName + "님이 "+relatedUserName+" 님의 " + relatedPosterName + " 글을 좋아합니다.";
        }
        if(activityType==ActivityType.POST){
            return activityUserName + "님이 글 " + relatedPosterName + "을 작성하셨습니다.";
        }
        return "";
    }
}
