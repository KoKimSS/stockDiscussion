package com.kss.stockDiscussion.service.newsFeedService;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetFollowingNewsFeedDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedDto;
import org.springframework.stereotype.Component;

@Component
public class NewsFeedMapper {

    public GetMyNewsFeedDto toGetMyNewsFeedDto(NewsFeed newsFeed) {
        User activityUser = newsFeed.getActivityUser();
        User relatedUser = newsFeed.getRelatedUser();
        Poster relatedPoster = newsFeed.getRelatedPoster();
        ActivityType activityType = newsFeed.getActivityType();
        return new GetMyNewsFeedDto(
                activityUser.getId(),
                activityUser.getName(),
                relatedUser.getId(),
                relatedUser.getName(),
                (relatedPoster != null) ? relatedPoster.getId() : null,
                (relatedPoster != null) ? relatedPoster.getTitle() : null,
                activityType
        );
    }

    public GetFollowingNewsFeedDto toGetFollowingNewsFeedDto(NewsFeed newsFeed) {
        User activityUser = newsFeed.getActivityUser();
        User relatedUser = newsFeed.getRelatedUser();
        Poster relatedPoster = newsFeed.getRelatedPoster();
        ActivityType activityType = newsFeed.getActivityType();
        return new GetFollowingNewsFeedDto(
                activityUser.getId(),
                activityUser.getName(),
                relatedUser.getId(),
                relatedUser.getName(),
                (relatedPoster != null) ? relatedPoster.getId() : null,
                (relatedPoster != null) ? relatedPoster.getTitle() : null,
                activityType
        );
    }


}
