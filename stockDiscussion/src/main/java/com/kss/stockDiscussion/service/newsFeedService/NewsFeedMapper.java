package com.kss.stockDiscussion.service.newsFeedService;

import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedDto;
import org.springframework.stereotype.Component;

@Component
public class NewsFeedMapper {

    public GetMyNewsFeedDto toGetMyNewsFeedDto(NewsFeed newsFeed) {
        User user = newsFeed.getUser();
        User activityUser = newsFeed.getActivityUser();
        User relatedUser = newsFeed.getRelatedUser();
        Poster relatedPoster = newsFeed.getRelatedPoster();
        NewsFeedType newsFeedType = newsFeed.getNewsFeedType();
        GetMyNewsFeedDto dto = GetMyNewsFeedDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .activityUserId(activityUser.getId())
                .activityUserName(activityUser.getName())
                .relatedUserId((relatedUser != null) ? relatedUser.getId() : null)
                .relatedUserName((relatedUser != null) ? relatedUser.getName() : null)
                .relatedPosterId((relatedPoster != null) ? relatedPoster.getId() : null)
                .relatedPosterName((relatedPoster != null) ? relatedPoster.getTitle() : null)
                .newsFeedType(newsFeedType).build();
        System.out.println(dto.getMessage());
        return dto;
    }

}
