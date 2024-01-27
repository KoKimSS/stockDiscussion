package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.service.followService.FollowService;
import com.kss.stockDiscussion.web.dto.request.follow.StartFollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;


    @PostMapping("/start-follow")
    ResponseEntity<? super StartFollowResponseDto> startFollow(
            @RequestBody @Valid StartFollowRequestDto requestBody,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Long loginId = principalDetails.getUser().getId();
        Long userId = requestBody.getFollowerId();
        if (loginId != userId) return StartFollowResponseDto.certificationFail();

        ResponseEntity<? super StartFollowResponseDto> response = followService.follow(requestBody);
        return response;
    }
}
