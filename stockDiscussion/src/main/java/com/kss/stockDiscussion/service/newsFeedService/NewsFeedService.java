package com.kss.stockDiscussion.service.newsFeedService;


import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import org.springframework.http.ResponseEntity;

public interface NewsFeedService {
//    ResponseEntity<? super CreateNewsFeedResponseDto> createNewsFeed(CreateNewsFeedRequestDto dto);

    ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeeds(GetMyNewsFeedRequestDto dto);
}
