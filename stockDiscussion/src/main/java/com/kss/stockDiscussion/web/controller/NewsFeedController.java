package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/get-myNewsFeed")
    ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid@RequestBody GetMyNewsFeedRequestDto requestBody
    ) {
        Long loginUserId = principalDetails.getUser().getId();
        Long requestUserId = requestBody.getUserId();
        if(loginUserId != requestUserId) return ResponseDto.certificationFail();

        ResponseEntity<? super GetMyNewsFeedResponseDto> response = newsFeedService.getMyNewsFeeds(requestBody);
        return response;
    }

}
