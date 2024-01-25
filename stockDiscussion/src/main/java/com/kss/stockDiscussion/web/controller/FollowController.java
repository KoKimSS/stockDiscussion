package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.followService.FollowService;
import com.kss.stockDiscussion.web.dto.request.follow.FollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.FollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/start-follow")
    ResponseEntity<? super FollowResponseDto> startFollow(
            @RequestBody @Valid FollowRequestDto requestBody
    ) {
        ResponseEntity<? super FollowResponseDto> response = followService.follow(requestBody);
        return response;
    }
}
