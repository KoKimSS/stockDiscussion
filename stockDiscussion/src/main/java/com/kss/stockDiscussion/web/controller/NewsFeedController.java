package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/newsFeed")
@RequiredArgsConstructor
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    @GetMapping("/myNewsFeed")
    ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeed(
            @Valid@RequestBody GetMyNewsFeedRequestDto requestBody
            ) {
        ResponseEntity<? super GetMyNewsFeedResponseDto> response = newsFeedService.getMyNewsFeeds(requestBody);
        return response;
    }

}
