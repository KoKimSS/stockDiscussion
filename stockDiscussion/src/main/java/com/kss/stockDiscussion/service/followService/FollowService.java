package com.kss.stockDiscussion.service.followService;


import com.kss.stockDiscussion.web.dto.request.follow.FollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.FollowResponseDto;
import org.springframework.http.ResponseEntity;

public interface FollowService {

    ResponseEntity<? super FollowResponseDto> follow(FollowRequestDto dto);
}
