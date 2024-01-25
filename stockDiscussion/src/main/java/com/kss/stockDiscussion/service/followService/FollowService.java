package com.kss.stockDiscussion.service.followService;


import com.kss.stockDiscussion.web.dto.request.follow.StartFollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
import org.springframework.http.ResponseEntity;

public interface FollowService {

    ResponseEntity<? super StartFollowResponseDto> follow(StartFollowRequestDto dto);
}
