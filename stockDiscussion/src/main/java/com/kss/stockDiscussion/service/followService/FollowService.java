package com.kss.stockDiscussion.service.followService;


import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.web.dto.request.follow.StartFollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FollowService {
    ResponseEntity<? super StartFollowResponseDto> follow(StartFollowRequestDto dto);
}
